package com.micropole.dqb.web.rest;

import com.micropole.dqb.DataQualityBoosterApp;

import com.micropole.dqb.domain.Pickle;
import com.micropole.dqb.repository.PickleRepository;
import com.micropole.dqb.service.PickleService;
import com.micropole.dqb.repository.search.PickleSearchRepository;
import com.micropole.dqb.service.dto.PickleDTO;
import com.micropole.dqb.service.mapper.PickleMapper;
import com.micropole.dqb.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

import javax.persistence.EntityManager;
import java.util.List;

import static com.micropole.dqb.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the PickleResource REST controller.
 *
 * @see PickleResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DataQualityBoosterApp.class)
public class PickleResourceIntTest {

    private static final String DEFAULT_NAME_FIELD = "AAAAAAAAAA";
    private static final String UPDATED_NAME_FIELD = "BBBBBBBBBB";

    private static final byte[] DEFAULT_FILE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_FILE = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_FILE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_FILE_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_PATH = "AAAAAAAAAA";
    private static final String UPDATED_PATH = "BBBBBBBBBB";

    @Autowired
    private PickleRepository pickleRepository;

    @Autowired
    private PickleMapper pickleMapper;

    @Autowired
    private PickleService pickleService;

    @Autowired
    private PickleSearchRepository pickleSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPickleMockMvc;

    private Pickle pickle;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PickleResource pickleResource = new PickleResource(pickleService);
        this.restPickleMockMvc = MockMvcBuilders.standaloneSetup(pickleResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Pickle createEntity(EntityManager em) {
        Pickle pickle = new Pickle()
            .nameField(DEFAULT_NAME_FIELD)
            .file(DEFAULT_FILE)
            .fileContentType(DEFAULT_FILE_CONTENT_TYPE)
            .path(DEFAULT_PATH);
        return pickle;
    }

    @Before
    public void initTest() {
        pickleSearchRepository.deleteAll();
        pickle = createEntity(em);
    }

    @Test
    @Transactional
    public void createPickle() throws Exception {
        int databaseSizeBeforeCreate = pickleRepository.findAll().size();

        // Create the Pickle
        PickleDTO pickleDTO = pickleMapper.toDto(pickle);
        restPickleMockMvc.perform(post("/api/pickles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pickleDTO)))
            .andExpect(status().isCreated());

        // Validate the Pickle in the database
        List<Pickle> pickleList = pickleRepository.findAll();
        assertThat(pickleList).hasSize(databaseSizeBeforeCreate + 1);
        Pickle testPickle = pickleList.get(pickleList.size() - 1);
        assertThat(testPickle.getNameField()).isEqualTo(DEFAULT_NAME_FIELD);
        assertThat(testPickle.getFile()).isEqualTo(DEFAULT_FILE);
        assertThat(testPickle.getFileContentType()).isEqualTo(DEFAULT_FILE_CONTENT_TYPE);
        assertThat(testPickle.getPath()).isEqualTo(DEFAULT_PATH);

        // Validate the Pickle in Elasticsearch
        Pickle pickleEs = pickleSearchRepository.findOne(testPickle.getId());
        assertThat(pickleEs).isEqualToIgnoringGivenFields(testPickle);
    }

    @Test
    @Transactional
    public void createPickleWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = pickleRepository.findAll().size();

        // Create the Pickle with an existing ID
        pickle.setId(1L);
        PickleDTO pickleDTO = pickleMapper.toDto(pickle);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPickleMockMvc.perform(post("/api/pickles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pickleDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Pickle in the database
        List<Pickle> pickleList = pickleRepository.findAll();
        assertThat(pickleList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllPickles() throws Exception {
        // Initialize the database
        pickleRepository.saveAndFlush(pickle);

        // Get all the pickleList
        restPickleMockMvc.perform(get("/api/pickles?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pickle.getId().intValue())))
            .andExpect(jsonPath("$.[*].nameField").value(hasItem(DEFAULT_NAME_FIELD.toString())))
            .andExpect(jsonPath("$.[*].fileContentType").value(hasItem(DEFAULT_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].file").value(hasItem(Base64Utils.encodeToString(DEFAULT_FILE))))
            .andExpect(jsonPath("$.[*].path").value(hasItem(DEFAULT_PATH.toString())));
    }

    @Test
    @Transactional
    public void getPickle() throws Exception {
        // Initialize the database
        pickleRepository.saveAndFlush(pickle);

        // Get the pickle
        restPickleMockMvc.perform(get("/api/pickles/{id}", pickle.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(pickle.getId().intValue()))
            .andExpect(jsonPath("$.nameField").value(DEFAULT_NAME_FIELD.toString()))
            .andExpect(jsonPath("$.fileContentType").value(DEFAULT_FILE_CONTENT_TYPE))
            .andExpect(jsonPath("$.file").value(Base64Utils.encodeToString(DEFAULT_FILE)))
            .andExpect(jsonPath("$.path").value(DEFAULT_PATH.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPickle() throws Exception {
        // Get the pickle
        restPickleMockMvc.perform(get("/api/pickles/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePickle() throws Exception {
        // Initialize the database
        pickleRepository.saveAndFlush(pickle);
        pickleSearchRepository.save(pickle);
        int databaseSizeBeforeUpdate = pickleRepository.findAll().size();

        // Update the pickle
        Pickle updatedPickle = pickleRepository.findOne(pickle.getId());
        // Disconnect from session so that the updates on updatedPickle are not directly saved in db
        em.detach(updatedPickle);
        updatedPickle
            .nameField(UPDATED_NAME_FIELD)
            .file(UPDATED_FILE)
            .fileContentType(UPDATED_FILE_CONTENT_TYPE)
            .path(UPDATED_PATH);
        PickleDTO pickleDTO = pickleMapper.toDto(updatedPickle);

        restPickleMockMvc.perform(put("/api/pickles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pickleDTO)))
            .andExpect(status().isOk());

        // Validate the Pickle in the database
        List<Pickle> pickleList = pickleRepository.findAll();
        assertThat(pickleList).hasSize(databaseSizeBeforeUpdate);
        Pickle testPickle = pickleList.get(pickleList.size() - 1);
        assertThat(testPickle.getNameField()).isEqualTo(UPDATED_NAME_FIELD);
        assertThat(testPickle.getFile()).isEqualTo(UPDATED_FILE);
        assertThat(testPickle.getFileContentType()).isEqualTo(UPDATED_FILE_CONTENT_TYPE);
        assertThat(testPickle.getPath()).isEqualTo(UPDATED_PATH);

        // Validate the Pickle in Elasticsearch
        Pickle pickleEs = pickleSearchRepository.findOne(testPickle.getId());
        assertThat(pickleEs).isEqualToIgnoringGivenFields(testPickle);
    }

    @Test
    @Transactional
    public void updateNonExistingPickle() throws Exception {
        int databaseSizeBeforeUpdate = pickleRepository.findAll().size();

        // Create the Pickle
        PickleDTO pickleDTO = pickleMapper.toDto(pickle);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restPickleMockMvc.perform(put("/api/pickles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pickleDTO)))
            .andExpect(status().isCreated());

        // Validate the Pickle in the database
        List<Pickle> pickleList = pickleRepository.findAll();
        assertThat(pickleList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deletePickle() throws Exception {
        // Initialize the database
        pickleRepository.saveAndFlush(pickle);
        pickleSearchRepository.save(pickle);
        int databaseSizeBeforeDelete = pickleRepository.findAll().size();

        // Get the pickle
        restPickleMockMvc.perform(delete("/api/pickles/{id}", pickle.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean pickleExistsInEs = pickleSearchRepository.exists(pickle.getId());
        assertThat(pickleExistsInEs).isFalse();

        // Validate the database is empty
        List<Pickle> pickleList = pickleRepository.findAll();
        assertThat(pickleList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchPickle() throws Exception {
        // Initialize the database
        pickleRepository.saveAndFlush(pickle);
        pickleSearchRepository.save(pickle);

        // Search the pickle
        restPickleMockMvc.perform(get("/api/_search/pickles?query=id:" + pickle.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pickle.getId().intValue())))
            .andExpect(jsonPath("$.[*].nameField").value(hasItem(DEFAULT_NAME_FIELD.toString())))
            .andExpect(jsonPath("$.[*].fileContentType").value(hasItem(DEFAULT_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].file").value(hasItem(Base64Utils.encodeToString(DEFAULT_FILE))))
            .andExpect(jsonPath("$.[*].path").value(hasItem(DEFAULT_PATH.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Pickle.class);
        Pickle pickle1 = new Pickle();
        pickle1.setId(1L);
        Pickle pickle2 = new Pickle();
        pickle2.setId(pickle1.getId());
        assertThat(pickle1).isEqualTo(pickle2);
        pickle2.setId(2L);
        assertThat(pickle1).isNotEqualTo(pickle2);
        pickle1.setId(null);
        assertThat(pickle1).isNotEqualTo(pickle2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PickleDTO.class);
        PickleDTO pickleDTO1 = new PickleDTO();
        pickleDTO1.setId(1L);
        PickleDTO pickleDTO2 = new PickleDTO();
        assertThat(pickleDTO1).isNotEqualTo(pickleDTO2);
        pickleDTO2.setId(pickleDTO1.getId());
        assertThat(pickleDTO1).isEqualTo(pickleDTO2);
        pickleDTO2.setId(2L);
        assertThat(pickleDTO1).isNotEqualTo(pickleDTO2);
        pickleDTO1.setId(null);
        assertThat(pickleDTO1).isNotEqualTo(pickleDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(pickleMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(pickleMapper.fromId(null)).isNull();
    }
}
