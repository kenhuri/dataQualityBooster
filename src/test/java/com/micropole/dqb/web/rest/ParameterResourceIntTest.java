package com.micropole.dqb.web.rest;

import com.micropole.dqb.DataQualityBoosterApp;

import com.micropole.dqb.domain.Parameter;
import com.micropole.dqb.repository.ParameterRepository;
import com.micropole.dqb.service.ParameterService;
import com.micropole.dqb.repository.search.ParameterSearchRepository;
import com.micropole.dqb.service.dto.ParameterDTO;
import com.micropole.dqb.service.mapper.ParameterMapper;
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
 * Test class for the ParameterResource REST controller.
 *
 * @see ParameterResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DataQualityBoosterApp.class)
public class ParameterResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_VALUE = "BBBBBBBBBB";

    @Autowired
    private ParameterRepository parameterRepository;

    @Autowired
    private ParameterMapper parameterMapper;

    @Autowired
    private ParameterService parameterService;

    @Autowired
    private ParameterSearchRepository parameterSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restParameterMockMvc;

    private Parameter parameter;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ParameterResource parameterResource = new ParameterResource(parameterService);
        this.restParameterMockMvc = MockMvcBuilders.standaloneSetup(parameterResource)
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
    public static Parameter createEntity(EntityManager em) {
        Parameter parameter = new Parameter()
            .name(DEFAULT_NAME)
            .value(DEFAULT_VALUE);
        return parameter;
    }

    @Before
    public void initTest() {
        parameterSearchRepository.deleteAll();
        parameter = createEntity(em);
    }

    @Test
    @Transactional
    public void createParameter() throws Exception {
        int databaseSizeBeforeCreate = parameterRepository.findAll().size();

        // Create the Parameter
        ParameterDTO parameterDTO = parameterMapper.toDto(parameter);
        restParameterMockMvc.perform(post("/api/parameters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(parameterDTO)))
            .andExpect(status().isCreated());

        // Validate the Parameter in the database
        List<Parameter> parameterList = parameterRepository.findAll();
        assertThat(parameterList).hasSize(databaseSizeBeforeCreate + 1);
        Parameter testParameter = parameterList.get(parameterList.size() - 1);
        assertThat(testParameter.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testParameter.getValue()).isEqualTo(DEFAULT_VALUE);

        // Validate the Parameter in Elasticsearch
        Parameter parameterEs = parameterSearchRepository.findOne(testParameter.getId());
        assertThat(parameterEs).isEqualToIgnoringGivenFields(testParameter);
    }

    @Test
    @Transactional
    public void createParameterWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = parameterRepository.findAll().size();

        // Create the Parameter with an existing ID
        parameter.setId(1L);
        ParameterDTO parameterDTO = parameterMapper.toDto(parameter);

        // An entity with an existing ID cannot be created, so this API call must fail
        restParameterMockMvc.perform(post("/api/parameters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(parameterDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Parameter in the database
        List<Parameter> parameterList = parameterRepository.findAll();
        assertThat(parameterList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllParameters() throws Exception {
        // Initialize the database
        parameterRepository.saveAndFlush(parameter);

        // Get all the parameterList
        restParameterMockMvc.perform(get("/api/parameters?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(parameter.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE.toString())));
    }

    @Test
    @Transactional
    public void getParameter() throws Exception {
        // Initialize the database
        parameterRepository.saveAndFlush(parameter);

        // Get the parameter
        restParameterMockMvc.perform(get("/api/parameters/{id}", parameter.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(parameter.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingParameter() throws Exception {
        // Get the parameter
        restParameterMockMvc.perform(get("/api/parameters/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateParameter() throws Exception {
        // Initialize the database
        parameterRepository.saveAndFlush(parameter);
        parameterSearchRepository.save(parameter);
        int databaseSizeBeforeUpdate = parameterRepository.findAll().size();

        // Update the parameter
        Parameter updatedParameter = parameterRepository.findOne(parameter.getId());
        // Disconnect from session so that the updates on updatedParameter are not directly saved in db
        em.detach(updatedParameter);
        updatedParameter
            .name(UPDATED_NAME)
            .value(UPDATED_VALUE);
        ParameterDTO parameterDTO = parameterMapper.toDto(updatedParameter);

        restParameterMockMvc.perform(put("/api/parameters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(parameterDTO)))
            .andExpect(status().isOk());

        // Validate the Parameter in the database
        List<Parameter> parameterList = parameterRepository.findAll();
        assertThat(parameterList).hasSize(databaseSizeBeforeUpdate);
        Parameter testParameter = parameterList.get(parameterList.size() - 1);
        assertThat(testParameter.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testParameter.getValue()).isEqualTo(UPDATED_VALUE);

        // Validate the Parameter in Elasticsearch
        Parameter parameterEs = parameterSearchRepository.findOne(testParameter.getId());
        assertThat(parameterEs).isEqualToIgnoringGivenFields(testParameter);
    }

    @Test
    @Transactional
    public void updateNonExistingParameter() throws Exception {
        int databaseSizeBeforeUpdate = parameterRepository.findAll().size();

        // Create the Parameter
        ParameterDTO parameterDTO = parameterMapper.toDto(parameter);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restParameterMockMvc.perform(put("/api/parameters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(parameterDTO)))
            .andExpect(status().isCreated());

        // Validate the Parameter in the database
        List<Parameter> parameterList = parameterRepository.findAll();
        assertThat(parameterList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteParameter() throws Exception {
        // Initialize the database
        parameterRepository.saveAndFlush(parameter);
        parameterSearchRepository.save(parameter);
        int databaseSizeBeforeDelete = parameterRepository.findAll().size();

        // Get the parameter
        restParameterMockMvc.perform(delete("/api/parameters/{id}", parameter.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean parameterExistsInEs = parameterSearchRepository.exists(parameter.getId());
        assertThat(parameterExistsInEs).isFalse();

        // Validate the database is empty
        List<Parameter> parameterList = parameterRepository.findAll();
        assertThat(parameterList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchParameter() throws Exception {
        // Initialize the database
        parameterRepository.saveAndFlush(parameter);
        parameterSearchRepository.save(parameter);

        // Search the parameter
        restParameterMockMvc.perform(get("/api/_search/parameters?query=id:" + parameter.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(parameter.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Parameter.class);
        Parameter parameter1 = new Parameter();
        parameter1.setId(1L);
        Parameter parameter2 = new Parameter();
        parameter2.setId(parameter1.getId());
        assertThat(parameter1).isEqualTo(parameter2);
        parameter2.setId(2L);
        assertThat(parameter1).isNotEqualTo(parameter2);
        parameter1.setId(null);
        assertThat(parameter1).isNotEqualTo(parameter2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ParameterDTO.class);
        ParameterDTO parameterDTO1 = new ParameterDTO();
        parameterDTO1.setId(1L);
        ParameterDTO parameterDTO2 = new ParameterDTO();
        assertThat(parameterDTO1).isNotEqualTo(parameterDTO2);
        parameterDTO2.setId(parameterDTO1.getId());
        assertThat(parameterDTO1).isEqualTo(parameterDTO2);
        parameterDTO2.setId(2L);
        assertThat(parameterDTO1).isNotEqualTo(parameterDTO2);
        parameterDTO1.setId(null);
        assertThat(parameterDTO1).isNotEqualTo(parameterDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(parameterMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(parameterMapper.fromId(null)).isNull();
    }
}
