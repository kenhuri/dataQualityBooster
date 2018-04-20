package com.micropole.dqb.web.rest;

import com.micropole.dqb.DataQualityBoosterApp;

import com.micropole.dqb.domain.Context;
import com.micropole.dqb.repository.ContextRepository;
import com.micropole.dqb.service.ContextService;
import com.micropole.dqb.repository.search.ContextSearchRepository;
import com.micropole.dqb.service.dto.ContextDTO;
import com.micropole.dqb.service.mapper.ContextMapper;
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
 * Test class for the ContextResource REST controller.
 *
 * @see ContextResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DataQualityBoosterApp.class)
public class ContextResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CLIENT = "AAAAAAAAAA";
    private static final String UPDATED_CLIENT = "BBBBBBBBBB";

    private static final byte[] DEFAULT_IMAGE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMAGE = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_IMAGE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMAGE_CONTENT_TYPE = "image/png";

    @Autowired
    private ContextRepository contextRepository;

    @Autowired
    private ContextMapper contextMapper;

    @Autowired
    private ContextService contextService;

    @Autowired
    private ContextSearchRepository contextSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restContextMockMvc;

    private Context context;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ContextResource contextResource = new ContextResource(contextService);
        this.restContextMockMvc = MockMvcBuilders.standaloneSetup(contextResource)
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
    public static Context createEntity(EntityManager em) {
        Context context = new Context()
            .name(DEFAULT_NAME)
            .client(DEFAULT_CLIENT)
            .image(DEFAULT_IMAGE)
            .imageContentType(DEFAULT_IMAGE_CONTENT_TYPE);
        return context;
    }

    @Before
    public void initTest() {
        contextSearchRepository.deleteAll();
        context = createEntity(em);
    }

    @Test
    @Transactional
    public void createContext() throws Exception {
        int databaseSizeBeforeCreate = contextRepository.findAll().size();

        // Create the Context
        ContextDTO contextDTO = contextMapper.toDto(context);
        restContextMockMvc.perform(post("/api/contexts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(contextDTO)))
            .andExpect(status().isCreated());

        // Validate the Context in the database
        List<Context> contextList = contextRepository.findAll();
        assertThat(contextList).hasSize(databaseSizeBeforeCreate + 1);
        Context testContext = contextList.get(contextList.size() - 1);
        assertThat(testContext.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testContext.getClient()).isEqualTo(DEFAULT_CLIENT);
        assertThat(testContext.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testContext.getImageContentType()).isEqualTo(DEFAULT_IMAGE_CONTENT_TYPE);

        // Validate the Context in Elasticsearch
        Context contextEs = contextSearchRepository.findOne(testContext.getId());
        assertThat(contextEs).isEqualToIgnoringGivenFields(testContext);
    }

    @Test
    @Transactional
    public void createContextWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = contextRepository.findAll().size();

        // Create the Context with an existing ID
        context.setId(1L);
        ContextDTO contextDTO = contextMapper.toDto(context);

        // An entity with an existing ID cannot be created, so this API call must fail
        restContextMockMvc.perform(post("/api/contexts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(contextDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Context in the database
        List<Context> contextList = contextRepository.findAll();
        assertThat(contextList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllContexts() throws Exception {
        // Initialize the database
        contextRepository.saveAndFlush(context);

        // Get all the contextList
        restContextMockMvc.perform(get("/api/contexts?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(context.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].client").value(hasItem(DEFAULT_CLIENT.toString())))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))));
    }

    @Test
    @Transactional
    public void getContext() throws Exception {
        // Initialize the database
        contextRepository.saveAndFlush(context);

        // Get the context
        restContextMockMvc.perform(get("/api/contexts/{id}", context.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(context.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.client").value(DEFAULT_CLIENT.toString()))
            .andExpect(jsonPath("$.imageContentType").value(DEFAULT_IMAGE_CONTENT_TYPE))
            .andExpect(jsonPath("$.image").value(Base64Utils.encodeToString(DEFAULT_IMAGE)));
    }

    @Test
    @Transactional
    public void getNonExistingContext() throws Exception {
        // Get the context
        restContextMockMvc.perform(get("/api/contexts/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateContext() throws Exception {
        // Initialize the database
        contextRepository.saveAndFlush(context);
        contextSearchRepository.save(context);
        int databaseSizeBeforeUpdate = contextRepository.findAll().size();

        // Update the context
        Context updatedContext = contextRepository.findOne(context.getId());
        // Disconnect from session so that the updates on updatedContext are not directly saved in db
        em.detach(updatedContext);
        updatedContext
            .name(UPDATED_NAME)
            .client(UPDATED_CLIENT)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE);
        ContextDTO contextDTO = contextMapper.toDto(updatedContext);

        restContextMockMvc.perform(put("/api/contexts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(contextDTO)))
            .andExpect(status().isOk());

        // Validate the Context in the database
        List<Context> contextList = contextRepository.findAll();
        assertThat(contextList).hasSize(databaseSizeBeforeUpdate);
        Context testContext = contextList.get(contextList.size() - 1);
        assertThat(testContext.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testContext.getClient()).isEqualTo(UPDATED_CLIENT);
        assertThat(testContext.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testContext.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);

        // Validate the Context in Elasticsearch
        Context contextEs = contextSearchRepository.findOne(testContext.getId());
        assertThat(contextEs).isEqualToIgnoringGivenFields(testContext);
    }

    @Test
    @Transactional
    public void updateNonExistingContext() throws Exception {
        int databaseSizeBeforeUpdate = contextRepository.findAll().size();

        // Create the Context
        ContextDTO contextDTO = contextMapper.toDto(context);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restContextMockMvc.perform(put("/api/contexts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(contextDTO)))
            .andExpect(status().isCreated());

        // Validate the Context in the database
        List<Context> contextList = contextRepository.findAll();
        assertThat(contextList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteContext() throws Exception {
        // Initialize the database
        contextRepository.saveAndFlush(context);
        contextSearchRepository.save(context);
        int databaseSizeBeforeDelete = contextRepository.findAll().size();

        // Get the context
        restContextMockMvc.perform(delete("/api/contexts/{id}", context.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean contextExistsInEs = contextSearchRepository.exists(context.getId());
        assertThat(contextExistsInEs).isFalse();

        // Validate the database is empty
        List<Context> contextList = contextRepository.findAll();
        assertThat(contextList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchContext() throws Exception {
        // Initialize the database
        contextRepository.saveAndFlush(context);
        contextSearchRepository.save(context);

        // Search the context
        restContextMockMvc.perform(get("/api/_search/contexts?query=id:" + context.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(context.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].client").value(hasItem(DEFAULT_CLIENT.toString())))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Context.class);
        Context context1 = new Context();
        context1.setId(1L);
        Context context2 = new Context();
        context2.setId(context1.getId());
        assertThat(context1).isEqualTo(context2);
        context2.setId(2L);
        assertThat(context1).isNotEqualTo(context2);
        context1.setId(null);
        assertThat(context1).isNotEqualTo(context2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ContextDTO.class);
        ContextDTO contextDTO1 = new ContextDTO();
        contextDTO1.setId(1L);
        ContextDTO contextDTO2 = new ContextDTO();
        assertThat(contextDTO1).isNotEqualTo(contextDTO2);
        contextDTO2.setId(contextDTO1.getId());
        assertThat(contextDTO1).isEqualTo(contextDTO2);
        contextDTO2.setId(2L);
        assertThat(contextDTO1).isNotEqualTo(contextDTO2);
        contextDTO1.setId(null);
        assertThat(contextDTO1).isNotEqualTo(contextDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(contextMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(contextMapper.fromId(null)).isNull();
    }
}
