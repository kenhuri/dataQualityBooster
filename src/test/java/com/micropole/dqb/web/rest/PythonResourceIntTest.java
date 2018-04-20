package com.micropole.dqb.web.rest;

import com.micropole.dqb.DataQualityBoosterApp;

import com.micropole.dqb.domain.Python;
import com.micropole.dqb.repository.PythonRepository;
import com.micropole.dqb.service.PythonService;
import com.micropole.dqb.repository.search.PythonSearchRepository;
import com.micropole.dqb.service.dto.PythonDTO;
import com.micropole.dqb.service.mapper.PythonMapper;
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
 * Test class for the PythonResource REST controller.
 *
 * @see PythonResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DataQualityBoosterApp.class)
public class PythonResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_SERVER = "AAAAAAAAAA";
    private static final String UPDATED_SERVER = "BBBBBBBBBB";

    private static final String DEFAULT_LOGIN = "AAAAAAAAAA";
    private static final String UPDATED_LOGIN = "BBBBBBBBBB";

    private static final String DEFAULT_KEY_SSH = "AAAAAAAAAA";
    private static final String UPDATED_KEY_SSH = "BBBBBBBBBB";

    private static final String DEFAULT_DEFAULT_PARAMETER = "AAAAAAAAAA";
    private static final String UPDATED_DEFAULT_PARAMETER = "BBBBBBBBBB";

    @Autowired
    private PythonRepository pythonRepository;

    @Autowired
    private PythonMapper pythonMapper;

    @Autowired
    private PythonService pythonService;

    @Autowired
    private PythonSearchRepository pythonSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPythonMockMvc;

    private Python python;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PythonResource pythonResource = new PythonResource(pythonService);
        this.restPythonMockMvc = MockMvcBuilders.standaloneSetup(pythonResource)
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
    public static Python createEntity(EntityManager em) {
        Python python = new Python()
            .name(DEFAULT_NAME)
            .server(DEFAULT_SERVER)
            .login(DEFAULT_LOGIN)
            .keySSH(DEFAULT_KEY_SSH)
            .defaultParameter(DEFAULT_DEFAULT_PARAMETER);
        return python;
    }

    @Before
    public void initTest() {
        pythonSearchRepository.deleteAll();
        python = createEntity(em);
    }

    @Test
    @Transactional
    public void createPython() throws Exception {
        int databaseSizeBeforeCreate = pythonRepository.findAll().size();

        // Create the Python
        PythonDTO pythonDTO = pythonMapper.toDto(python);
        restPythonMockMvc.perform(post("/api/pythons")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pythonDTO)))
            .andExpect(status().isCreated());

        // Validate the Python in the database
        List<Python> pythonList = pythonRepository.findAll();
        assertThat(pythonList).hasSize(databaseSizeBeforeCreate + 1);
        Python testPython = pythonList.get(pythonList.size() - 1);
        assertThat(testPython.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPython.getServer()).isEqualTo(DEFAULT_SERVER);
        assertThat(testPython.getLogin()).isEqualTo(DEFAULT_LOGIN);
        assertThat(testPython.getKeySSH()).isEqualTo(DEFAULT_KEY_SSH);
        assertThat(testPython.getDefaultParameter()).isEqualTo(DEFAULT_DEFAULT_PARAMETER);

        // Validate the Python in Elasticsearch
        Python pythonEs = pythonSearchRepository.findOne(testPython.getId());
        assertThat(pythonEs).isEqualToIgnoringGivenFields(testPython);
    }

    @Test
    @Transactional
    public void createPythonWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = pythonRepository.findAll().size();

        // Create the Python with an existing ID
        python.setId(1L);
        PythonDTO pythonDTO = pythonMapper.toDto(python);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPythonMockMvc.perform(post("/api/pythons")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pythonDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Python in the database
        List<Python> pythonList = pythonRepository.findAll();
        assertThat(pythonList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllPythons() throws Exception {
        // Initialize the database
        pythonRepository.saveAndFlush(python);

        // Get all the pythonList
        restPythonMockMvc.perform(get("/api/pythons?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(python.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].server").value(hasItem(DEFAULT_SERVER.toString())))
            .andExpect(jsonPath("$.[*].login").value(hasItem(DEFAULT_LOGIN.toString())))
            .andExpect(jsonPath("$.[*].keySSH").value(hasItem(DEFAULT_KEY_SSH.toString())))
            .andExpect(jsonPath("$.[*].defaultParameter").value(hasItem(DEFAULT_DEFAULT_PARAMETER.toString())));
    }

    @Test
    @Transactional
    public void getPython() throws Exception {
        // Initialize the database
        pythonRepository.saveAndFlush(python);

        // Get the python
        restPythonMockMvc.perform(get("/api/pythons/{id}", python.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(python.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.server").value(DEFAULT_SERVER.toString()))
            .andExpect(jsonPath("$.login").value(DEFAULT_LOGIN.toString()))
            .andExpect(jsonPath("$.keySSH").value(DEFAULT_KEY_SSH.toString()))
            .andExpect(jsonPath("$.defaultParameter").value(DEFAULT_DEFAULT_PARAMETER.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPython() throws Exception {
        // Get the python
        restPythonMockMvc.perform(get("/api/pythons/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePython() throws Exception {
        // Initialize the database
        pythonRepository.saveAndFlush(python);
        pythonSearchRepository.save(python);
        int databaseSizeBeforeUpdate = pythonRepository.findAll().size();

        // Update the python
        Python updatedPython = pythonRepository.findOne(python.getId());
        // Disconnect from session so that the updates on updatedPython are not directly saved in db
        em.detach(updatedPython);
        updatedPython
            .name(UPDATED_NAME)
            .server(UPDATED_SERVER)
            .login(UPDATED_LOGIN)
            .keySSH(UPDATED_KEY_SSH)
            .defaultParameter(UPDATED_DEFAULT_PARAMETER);
        PythonDTO pythonDTO = pythonMapper.toDto(updatedPython);

        restPythonMockMvc.perform(put("/api/pythons")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pythonDTO)))
            .andExpect(status().isOk());

        // Validate the Python in the database
        List<Python> pythonList = pythonRepository.findAll();
        assertThat(pythonList).hasSize(databaseSizeBeforeUpdate);
        Python testPython = pythonList.get(pythonList.size() - 1);
        assertThat(testPython.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPython.getServer()).isEqualTo(UPDATED_SERVER);
        assertThat(testPython.getLogin()).isEqualTo(UPDATED_LOGIN);
        assertThat(testPython.getKeySSH()).isEqualTo(UPDATED_KEY_SSH);
        assertThat(testPython.getDefaultParameter()).isEqualTo(UPDATED_DEFAULT_PARAMETER);

        // Validate the Python in Elasticsearch
        Python pythonEs = pythonSearchRepository.findOne(testPython.getId());
        assertThat(pythonEs).isEqualToIgnoringGivenFields(testPython);
    }

    @Test
    @Transactional
    public void updateNonExistingPython() throws Exception {
        int databaseSizeBeforeUpdate = pythonRepository.findAll().size();

        // Create the Python
        PythonDTO pythonDTO = pythonMapper.toDto(python);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restPythonMockMvc.perform(put("/api/pythons")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pythonDTO)))
            .andExpect(status().isCreated());

        // Validate the Python in the database
        List<Python> pythonList = pythonRepository.findAll();
        assertThat(pythonList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deletePython() throws Exception {
        // Initialize the database
        pythonRepository.saveAndFlush(python);
        pythonSearchRepository.save(python);
        int databaseSizeBeforeDelete = pythonRepository.findAll().size();

        // Get the python
        restPythonMockMvc.perform(delete("/api/pythons/{id}", python.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean pythonExistsInEs = pythonSearchRepository.exists(python.getId());
        assertThat(pythonExistsInEs).isFalse();

        // Validate the database is empty
        List<Python> pythonList = pythonRepository.findAll();
        assertThat(pythonList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchPython() throws Exception {
        // Initialize the database
        pythonRepository.saveAndFlush(python);
        pythonSearchRepository.save(python);

        // Search the python
        restPythonMockMvc.perform(get("/api/_search/pythons?query=id:" + python.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(python.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].server").value(hasItem(DEFAULT_SERVER.toString())))
            .andExpect(jsonPath("$.[*].login").value(hasItem(DEFAULT_LOGIN.toString())))
            .andExpect(jsonPath("$.[*].keySSH").value(hasItem(DEFAULT_KEY_SSH.toString())))
            .andExpect(jsonPath("$.[*].defaultParameter").value(hasItem(DEFAULT_DEFAULT_PARAMETER.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Python.class);
        Python python1 = new Python();
        python1.setId(1L);
        Python python2 = new Python();
        python2.setId(python1.getId());
        assertThat(python1).isEqualTo(python2);
        python2.setId(2L);
        assertThat(python1).isNotEqualTo(python2);
        python1.setId(null);
        assertThat(python1).isNotEqualTo(python2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PythonDTO.class);
        PythonDTO pythonDTO1 = new PythonDTO();
        pythonDTO1.setId(1L);
        PythonDTO pythonDTO2 = new PythonDTO();
        assertThat(pythonDTO1).isNotEqualTo(pythonDTO2);
        pythonDTO2.setId(pythonDTO1.getId());
        assertThat(pythonDTO1).isEqualTo(pythonDTO2);
        pythonDTO2.setId(2L);
        assertThat(pythonDTO1).isNotEqualTo(pythonDTO2);
        pythonDTO1.setId(null);
        assertThat(pythonDTO1).isNotEqualTo(pythonDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(pythonMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(pythonMapper.fromId(null)).isNull();
    }
}
