package com.micropole.dqb.web.rest;

import com.micropole.dqb.DataQualityBoosterApp;

import com.micropole.dqb.domain.Execution;
import com.micropole.dqb.repository.ExecutionRepository;
import com.micropole.dqb.service.ExecutionService;
import com.micropole.dqb.repository.search.ExecutionSearchRepository;
import com.micropole.dqb.service.dto.ExecutionDTO;
import com.micropole.dqb.service.mapper.ExecutionMapper;
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
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static com.micropole.dqb.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.micropole.dqb.domain.enumeration.Status;
/**
 * Test class for the ExecutionResource REST controller.
 *
 * @see ExecutionResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DataQualityBoosterApp.class)
public class ExecutionResourceIntTest {

    private static final Instant DEFAULT_START_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_START_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_END_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_END_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Status DEFAULT_STATUS = Status.STARTED;
    private static final Status UPDATED_STATUS = Status.FINISHED;

    private static final byte[] DEFAULT_INPUT_FILE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_INPUT_FILE = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_INPUT_FILE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_INPUT_FILE_CONTENT_TYPE = "image/png";

    private static final byte[] DEFAULT_OUTPUT_FILE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_OUTPUT_FILE = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_OUTPUT_FILE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_OUTPUT_FILE_CONTENT_TYPE = "image/png";

    private static final byte[] DEFAULT_LOG_FILE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_LOG_FILE = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_LOG_FILE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_LOG_FILE_CONTENT_TYPE = "image/png";

    private static final Boolean DEFAULT_OPTIMIZE = false;
    private static final Boolean UPDATED_OPTIMIZE = true;

    private static final Boolean DEFAULT_TRAIN = false;
    private static final Boolean UPDATED_TRAIN = true;

    private static final Boolean DEFAULT_ALLOCATION = false;
    private static final Boolean UPDATED_ALLOCATION = true;

    private static final String DEFAULT_COMMENTARY = "AAAAAAAAAA";
    private static final String UPDATED_COMMENTARY = "BBBBBBBBBB";

    private static final String DEFAULT_USER_ID = "AAAAAAAAAA";
    private static final String UPDATED_USER_ID = "BBBBBBBBBB";

    @Autowired
    private ExecutionRepository executionRepository;

    @Autowired
    private ExecutionMapper executionMapper;

    @Autowired
    private ExecutionService executionService;

    @Autowired
    private ExecutionSearchRepository executionSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restExecutionMockMvc;

    private Execution execution;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ExecutionResource executionResource = new ExecutionResource(executionService);
        this.restExecutionMockMvc = MockMvcBuilders.standaloneSetup(executionResource)
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
    public static Execution createEntity(EntityManager em) {
        Execution execution = new Execution()
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .status(DEFAULT_STATUS)
            .inputFile(DEFAULT_INPUT_FILE)
            .inputFileContentType(DEFAULT_INPUT_FILE_CONTENT_TYPE)
            .outputFile(DEFAULT_OUTPUT_FILE)
            .outputFileContentType(DEFAULT_OUTPUT_FILE_CONTENT_TYPE)
            .logFile(DEFAULT_LOG_FILE)
            .logFileContentType(DEFAULT_LOG_FILE_CONTENT_TYPE)
            .optimize(DEFAULT_OPTIMIZE)
            .train(DEFAULT_TRAIN)
            .allocation(DEFAULT_ALLOCATION)
            .commentary(DEFAULT_COMMENTARY)
            .userId(DEFAULT_USER_ID);
        return execution;
    }

    @Before
    public void initTest() {
        executionSearchRepository.deleteAll();
        execution = createEntity(em);
    }

    @Test
    @Transactional
    public void createExecution() throws Exception {
        int databaseSizeBeforeCreate = executionRepository.findAll().size();

        // Create the Execution
        ExecutionDTO executionDTO = executionMapper.toDto(execution);
        restExecutionMockMvc.perform(post("/api/executions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(executionDTO)))
            .andExpect(status().isCreated());

        // Validate the Execution in the database
        List<Execution> executionList = executionRepository.findAll();
        assertThat(executionList).hasSize(databaseSizeBeforeCreate + 1);
        Execution testExecution = executionList.get(executionList.size() - 1);
        assertThat(testExecution.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testExecution.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testExecution.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testExecution.getInputFile()).isEqualTo(DEFAULT_INPUT_FILE);
        assertThat(testExecution.getInputFileContentType()).isEqualTo(DEFAULT_INPUT_FILE_CONTENT_TYPE);
        assertThat(testExecution.getOutputFile()).isEqualTo(DEFAULT_OUTPUT_FILE);
        assertThat(testExecution.getOutputFileContentType()).isEqualTo(DEFAULT_OUTPUT_FILE_CONTENT_TYPE);
        assertThat(testExecution.getLogFile()).isEqualTo(DEFAULT_LOG_FILE);
        assertThat(testExecution.getLogFileContentType()).isEqualTo(DEFAULT_LOG_FILE_CONTENT_TYPE);
        assertThat(testExecution.isOptimize()).isEqualTo(DEFAULT_OPTIMIZE);
        assertThat(testExecution.isTrain()).isEqualTo(DEFAULT_TRAIN);
        assertThat(testExecution.isAllocation()).isEqualTo(DEFAULT_ALLOCATION);
        assertThat(testExecution.getCommentary()).isEqualTo(DEFAULT_COMMENTARY);
        assertThat(testExecution.getUserId()).isEqualTo(DEFAULT_USER_ID);

        // Validate the Execution in Elasticsearch
        Execution executionEs = executionSearchRepository.findOne(testExecution.getId());
        assertThat(executionEs).isEqualToIgnoringGivenFields(testExecution);
    }

    @Test
    @Transactional
    public void createExecutionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = executionRepository.findAll().size();

        // Create the Execution with an existing ID
        execution.setId(1L);
        ExecutionDTO executionDTO = executionMapper.toDto(execution);

        // An entity with an existing ID cannot be created, so this API call must fail
        restExecutionMockMvc.perform(post("/api/executions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(executionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Execution in the database
        List<Execution> executionList = executionRepository.findAll();
        assertThat(executionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllExecutions() throws Exception {
        // Initialize the database
        executionRepository.saveAndFlush(execution);

        // Get all the executionList
        restExecutionMockMvc.perform(get("/api/executions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(execution.getId().intValue())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].inputFileContentType").value(hasItem(DEFAULT_INPUT_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].inputFile").value(hasItem(Base64Utils.encodeToString(DEFAULT_INPUT_FILE))))
            .andExpect(jsonPath("$.[*].outputFileContentType").value(hasItem(DEFAULT_OUTPUT_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].outputFile").value(hasItem(Base64Utils.encodeToString(DEFAULT_OUTPUT_FILE))))
            .andExpect(jsonPath("$.[*].logFileContentType").value(hasItem(DEFAULT_LOG_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].logFile").value(hasItem(Base64Utils.encodeToString(DEFAULT_LOG_FILE))))
            .andExpect(jsonPath("$.[*].optimize").value(hasItem(DEFAULT_OPTIMIZE.booleanValue())))
            .andExpect(jsonPath("$.[*].train").value(hasItem(DEFAULT_TRAIN.booleanValue())))
            .andExpect(jsonPath("$.[*].allocation").value(hasItem(DEFAULT_ALLOCATION.booleanValue())))
            .andExpect(jsonPath("$.[*].commentary").value(hasItem(DEFAULT_COMMENTARY.toString())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.toString())));
    }

    @Test
    @Transactional
    public void getExecution() throws Exception {
        // Initialize the database
        executionRepository.saveAndFlush(execution);

        // Get the execution
        restExecutionMockMvc.perform(get("/api/executions/{id}", execution.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(execution.getId().intValue()))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.inputFileContentType").value(DEFAULT_INPUT_FILE_CONTENT_TYPE))
            .andExpect(jsonPath("$.inputFile").value(Base64Utils.encodeToString(DEFAULT_INPUT_FILE)))
            .andExpect(jsonPath("$.outputFileContentType").value(DEFAULT_OUTPUT_FILE_CONTENT_TYPE))
            .andExpect(jsonPath("$.outputFile").value(Base64Utils.encodeToString(DEFAULT_OUTPUT_FILE)))
            .andExpect(jsonPath("$.logFileContentType").value(DEFAULT_LOG_FILE_CONTENT_TYPE))
            .andExpect(jsonPath("$.logFile").value(Base64Utils.encodeToString(DEFAULT_LOG_FILE)))
            .andExpect(jsonPath("$.optimize").value(DEFAULT_OPTIMIZE.booleanValue()))
            .andExpect(jsonPath("$.train").value(DEFAULT_TRAIN.booleanValue()))
            .andExpect(jsonPath("$.allocation").value(DEFAULT_ALLOCATION.booleanValue()))
            .andExpect(jsonPath("$.commentary").value(DEFAULT_COMMENTARY.toString()))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingExecution() throws Exception {
        // Get the execution
        restExecutionMockMvc.perform(get("/api/executions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateExecution() throws Exception {
        // Initialize the database
        executionRepository.saveAndFlush(execution);
        executionSearchRepository.save(execution);
        int databaseSizeBeforeUpdate = executionRepository.findAll().size();

        // Update the execution
        Execution updatedExecution = executionRepository.findOne(execution.getId());
        // Disconnect from session so that the updates on updatedExecution are not directly saved in db
        em.detach(updatedExecution);
        updatedExecution
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .status(UPDATED_STATUS)
            .inputFile(UPDATED_INPUT_FILE)
            .inputFileContentType(UPDATED_INPUT_FILE_CONTENT_TYPE)
            .outputFile(UPDATED_OUTPUT_FILE)
            .outputFileContentType(UPDATED_OUTPUT_FILE_CONTENT_TYPE)
            .logFile(UPDATED_LOG_FILE)
            .logFileContentType(UPDATED_LOG_FILE_CONTENT_TYPE)
            .optimize(UPDATED_OPTIMIZE)
            .train(UPDATED_TRAIN)
            .allocation(UPDATED_ALLOCATION)
            .commentary(UPDATED_COMMENTARY)
            .userId(UPDATED_USER_ID);
        ExecutionDTO executionDTO = executionMapper.toDto(updatedExecution);

        restExecutionMockMvc.perform(put("/api/executions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(executionDTO)))
            .andExpect(status().isOk());

        // Validate the Execution in the database
        List<Execution> executionList = executionRepository.findAll();
        assertThat(executionList).hasSize(databaseSizeBeforeUpdate);
        Execution testExecution = executionList.get(executionList.size() - 1);
        assertThat(testExecution.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testExecution.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testExecution.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testExecution.getInputFile()).isEqualTo(UPDATED_INPUT_FILE);
        assertThat(testExecution.getInputFileContentType()).isEqualTo(UPDATED_INPUT_FILE_CONTENT_TYPE);
        assertThat(testExecution.getOutputFile()).isEqualTo(UPDATED_OUTPUT_FILE);
        assertThat(testExecution.getOutputFileContentType()).isEqualTo(UPDATED_OUTPUT_FILE_CONTENT_TYPE);
        assertThat(testExecution.getLogFile()).isEqualTo(UPDATED_LOG_FILE);
        assertThat(testExecution.getLogFileContentType()).isEqualTo(UPDATED_LOG_FILE_CONTENT_TYPE);
        assertThat(testExecution.isOptimize()).isEqualTo(UPDATED_OPTIMIZE);
        assertThat(testExecution.isTrain()).isEqualTo(UPDATED_TRAIN);
        assertThat(testExecution.isAllocation()).isEqualTo(UPDATED_ALLOCATION);
        assertThat(testExecution.getCommentary()).isEqualTo(UPDATED_COMMENTARY);
        assertThat(testExecution.getUserId()).isEqualTo(UPDATED_USER_ID);

        // Validate the Execution in Elasticsearch
        Execution executionEs = executionSearchRepository.findOne(testExecution.getId());
        assertThat(executionEs).isEqualToIgnoringGivenFields(testExecution);
    }

    @Test
    @Transactional
    public void updateNonExistingExecution() throws Exception {
        int databaseSizeBeforeUpdate = executionRepository.findAll().size();

        // Create the Execution
        ExecutionDTO executionDTO = executionMapper.toDto(execution);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restExecutionMockMvc.perform(put("/api/executions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(executionDTO)))
            .andExpect(status().isCreated());

        // Validate the Execution in the database
        List<Execution> executionList = executionRepository.findAll();
        assertThat(executionList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteExecution() throws Exception {
        // Initialize the database
        executionRepository.saveAndFlush(execution);
        executionSearchRepository.save(execution);
        int databaseSizeBeforeDelete = executionRepository.findAll().size();

        // Get the execution
        restExecutionMockMvc.perform(delete("/api/executions/{id}", execution.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean executionExistsInEs = executionSearchRepository.exists(execution.getId());
        assertThat(executionExistsInEs).isFalse();

        // Validate the database is empty
        List<Execution> executionList = executionRepository.findAll();
        assertThat(executionList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchExecution() throws Exception {
        // Initialize the database
        executionRepository.saveAndFlush(execution);
        executionSearchRepository.save(execution);

        // Search the execution
        restExecutionMockMvc.perform(get("/api/_search/executions?query=id:" + execution.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(execution.getId().intValue())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].inputFileContentType").value(hasItem(DEFAULT_INPUT_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].inputFile").value(hasItem(Base64Utils.encodeToString(DEFAULT_INPUT_FILE))))
            .andExpect(jsonPath("$.[*].outputFileContentType").value(hasItem(DEFAULT_OUTPUT_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].outputFile").value(hasItem(Base64Utils.encodeToString(DEFAULT_OUTPUT_FILE))))
            .andExpect(jsonPath("$.[*].logFileContentType").value(hasItem(DEFAULT_LOG_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].logFile").value(hasItem(Base64Utils.encodeToString(DEFAULT_LOG_FILE))))
            .andExpect(jsonPath("$.[*].optimize").value(hasItem(DEFAULT_OPTIMIZE.booleanValue())))
            .andExpect(jsonPath("$.[*].train").value(hasItem(DEFAULT_TRAIN.booleanValue())))
            .andExpect(jsonPath("$.[*].allocation").value(hasItem(DEFAULT_ALLOCATION.booleanValue())))
            .andExpect(jsonPath("$.[*].commentary").value(hasItem(DEFAULT_COMMENTARY.toString())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Execution.class);
        Execution execution1 = new Execution();
        execution1.setId(1L);
        Execution execution2 = new Execution();
        execution2.setId(execution1.getId());
        assertThat(execution1).isEqualTo(execution2);
        execution2.setId(2L);
        assertThat(execution1).isNotEqualTo(execution2);
        execution1.setId(null);
        assertThat(execution1).isNotEqualTo(execution2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ExecutionDTO.class);
        ExecutionDTO executionDTO1 = new ExecutionDTO();
        executionDTO1.setId(1L);
        ExecutionDTO executionDTO2 = new ExecutionDTO();
        assertThat(executionDTO1).isNotEqualTo(executionDTO2);
        executionDTO2.setId(executionDTO1.getId());
        assertThat(executionDTO1).isEqualTo(executionDTO2);
        executionDTO2.setId(2L);
        assertThat(executionDTO1).isNotEqualTo(executionDTO2);
        executionDTO1.setId(null);
        assertThat(executionDTO1).isNotEqualTo(executionDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(executionMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(executionMapper.fromId(null)).isNull();
    }
}
