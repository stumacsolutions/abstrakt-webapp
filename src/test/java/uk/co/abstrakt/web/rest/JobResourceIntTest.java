package uk.co.abstrakt.web.rest;

import uk.co.abstrakt.AbstraktApp;

import uk.co.abstrakt.domain.Job;
import uk.co.abstrakt.domain.Customer;
import uk.co.abstrakt.repository.JobRepository;
import uk.co.abstrakt.service.JobService;
import uk.co.abstrakt.web.rest.errors.ExceptionTranslator;
import uk.co.abstrakt.service.dto.JobCriteria;
import uk.co.abstrakt.service.JobQueryService;

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

import javax.persistence.EntityManager;
import java.util.List;


import static uk.co.abstrakt.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import uk.co.abstrakt.domain.enumeration.Week;
/**
 * Test class for the JobResource REST controller.
 *
 * @see JobResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AbstraktApp.class)
public class JobResourceIntTest {

    private static final Week DEFAULT_WEEK = Week.ONE;
    private static final Week UPDATED_WEEK = Week.TWO;

    private static final Boolean DEFAULT_COMPLETE = false;
    private static final Boolean UPDATED_COMPLETE = true;

    private static final Boolean DEFAULT_PAID = false;
    private static final Boolean UPDATED_PAID = true;

    @Autowired
    private JobRepository jobRepository;
    
    @Autowired
    private JobService jobService;

    @Autowired
    private JobQueryService jobQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restJobMockMvc;

    private Job job;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final JobResource jobResource = new JobResource(jobService, jobQueryService);
        this.restJobMockMvc = MockMvcBuilders.standaloneSetup(jobResource)
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
    public static Job createEntity(EntityManager em) {
        Job job = new Job()
            .week(DEFAULT_WEEK)
            .complete(DEFAULT_COMPLETE)
            .paid(DEFAULT_PAID);
        return job;
    }

    @Before
    public void initTest() {
        job = createEntity(em);
    }

    @Test
    @Transactional
    public void createJob() throws Exception {
        int databaseSizeBeforeCreate = jobRepository.findAll().size();

        // Create the Job
        restJobMockMvc.perform(post("/api/jobs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(job)))
            .andExpect(status().isCreated());

        // Validate the Job in the database
        List<Job> jobList = jobRepository.findAll();
        assertThat(jobList).hasSize(databaseSizeBeforeCreate + 1);
        Job testJob = jobList.get(jobList.size() - 1);
        assertThat(testJob.getWeek()).isEqualTo(DEFAULT_WEEK);
        assertThat(testJob.isComplete()).isEqualTo(DEFAULT_COMPLETE);
        assertThat(testJob.isPaid()).isEqualTo(DEFAULT_PAID);
    }

    @Test
    @Transactional
    public void createJobWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = jobRepository.findAll().size();

        // Create the Job with an existing ID
        job.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restJobMockMvc.perform(post("/api/jobs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(job)))
            .andExpect(status().isBadRequest());

        // Validate the Job in the database
        List<Job> jobList = jobRepository.findAll();
        assertThat(jobList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllJobs() throws Exception {
        // Initialize the database
        jobRepository.saveAndFlush(job);

        // Get all the jobList
        restJobMockMvc.perform(get("/api/jobs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(job.getId().intValue())))
            .andExpect(jsonPath("$.[*].week").value(hasItem(DEFAULT_WEEK.toString())))
            .andExpect(jsonPath("$.[*].complete").value(hasItem(DEFAULT_COMPLETE.booleanValue())))
            .andExpect(jsonPath("$.[*].paid").value(hasItem(DEFAULT_PAID.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getJob() throws Exception {
        // Initialize the database
        jobRepository.saveAndFlush(job);

        // Get the job
        restJobMockMvc.perform(get("/api/jobs/{id}", job.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(job.getId().intValue()))
            .andExpect(jsonPath("$.week").value(DEFAULT_WEEK.toString()))
            .andExpect(jsonPath("$.complete").value(DEFAULT_COMPLETE.booleanValue()))
            .andExpect(jsonPath("$.paid").value(DEFAULT_PAID.booleanValue()));
    }

    @Test
    @Transactional
    public void getAllJobsByWeekIsEqualToSomething() throws Exception {
        // Initialize the database
        jobRepository.saveAndFlush(job);

        // Get all the jobList where week equals to DEFAULT_WEEK
        defaultJobShouldBeFound("week.equals=" + DEFAULT_WEEK);

        // Get all the jobList where week equals to UPDATED_WEEK
        defaultJobShouldNotBeFound("week.equals=" + UPDATED_WEEK);
    }

    @Test
    @Transactional
    public void getAllJobsByWeekIsInShouldWork() throws Exception {
        // Initialize the database
        jobRepository.saveAndFlush(job);

        // Get all the jobList where week in DEFAULT_WEEK or UPDATED_WEEK
        defaultJobShouldBeFound("week.in=" + DEFAULT_WEEK + "," + UPDATED_WEEK);

        // Get all the jobList where week equals to UPDATED_WEEK
        defaultJobShouldNotBeFound("week.in=" + UPDATED_WEEK);
    }

    @Test
    @Transactional
    public void getAllJobsByWeekIsNullOrNotNull() throws Exception {
        // Initialize the database
        jobRepository.saveAndFlush(job);

        // Get all the jobList where week is not null
        defaultJobShouldBeFound("week.specified=true");

        // Get all the jobList where week is null
        defaultJobShouldNotBeFound("week.specified=false");
    }

    @Test
    @Transactional
    public void getAllJobsByCompleteIsEqualToSomething() throws Exception {
        // Initialize the database
        jobRepository.saveAndFlush(job);

        // Get all the jobList where complete equals to DEFAULT_COMPLETE
        defaultJobShouldBeFound("complete.equals=" + DEFAULT_COMPLETE);

        // Get all the jobList where complete equals to UPDATED_COMPLETE
        defaultJobShouldNotBeFound("complete.equals=" + UPDATED_COMPLETE);
    }

    @Test
    @Transactional
    public void getAllJobsByCompleteIsInShouldWork() throws Exception {
        // Initialize the database
        jobRepository.saveAndFlush(job);

        // Get all the jobList where complete in DEFAULT_COMPLETE or UPDATED_COMPLETE
        defaultJobShouldBeFound("complete.in=" + DEFAULT_COMPLETE + "," + UPDATED_COMPLETE);

        // Get all the jobList where complete equals to UPDATED_COMPLETE
        defaultJobShouldNotBeFound("complete.in=" + UPDATED_COMPLETE);
    }

    @Test
    @Transactional
    public void getAllJobsByCompleteIsNullOrNotNull() throws Exception {
        // Initialize the database
        jobRepository.saveAndFlush(job);

        // Get all the jobList where complete is not null
        defaultJobShouldBeFound("complete.specified=true");

        // Get all the jobList where complete is null
        defaultJobShouldNotBeFound("complete.specified=false");
    }

    @Test
    @Transactional
    public void getAllJobsByPaidIsEqualToSomething() throws Exception {
        // Initialize the database
        jobRepository.saveAndFlush(job);

        // Get all the jobList where paid equals to DEFAULT_PAID
        defaultJobShouldBeFound("paid.equals=" + DEFAULT_PAID);

        // Get all the jobList where paid equals to UPDATED_PAID
        defaultJobShouldNotBeFound("paid.equals=" + UPDATED_PAID);
    }

    @Test
    @Transactional
    public void getAllJobsByPaidIsInShouldWork() throws Exception {
        // Initialize the database
        jobRepository.saveAndFlush(job);

        // Get all the jobList where paid in DEFAULT_PAID or UPDATED_PAID
        defaultJobShouldBeFound("paid.in=" + DEFAULT_PAID + "," + UPDATED_PAID);

        // Get all the jobList where paid equals to UPDATED_PAID
        defaultJobShouldNotBeFound("paid.in=" + UPDATED_PAID);
    }

    @Test
    @Transactional
    public void getAllJobsByPaidIsNullOrNotNull() throws Exception {
        // Initialize the database
        jobRepository.saveAndFlush(job);

        // Get all the jobList where paid is not null
        defaultJobShouldBeFound("paid.specified=true");

        // Get all the jobList where paid is null
        defaultJobShouldNotBeFound("paid.specified=false");
    }

    @Test
    @Transactional
    public void getAllJobsByCustomerIsEqualToSomething() throws Exception {
        // Initialize the database
        Customer customer = CustomerResourceIntTest.createEntity(em);
        em.persist(customer);
        em.flush();
        job.setCustomer(customer);
        jobRepository.saveAndFlush(job);
        Long customerId = customer.getId();

        // Get all the jobList where customer equals to customerId
        defaultJobShouldBeFound("customerId.equals=" + customerId);

        // Get all the jobList where customer equals to customerId + 1
        defaultJobShouldNotBeFound("customerId.equals=" + (customerId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultJobShouldBeFound(String filter) throws Exception {
        restJobMockMvc.perform(get("/api/jobs?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(job.getId().intValue())))
            .andExpect(jsonPath("$.[*].week").value(hasItem(DEFAULT_WEEK.toString())))
            .andExpect(jsonPath("$.[*].complete").value(hasItem(DEFAULT_COMPLETE.booleanValue())))
            .andExpect(jsonPath("$.[*].paid").value(hasItem(DEFAULT_PAID.booleanValue())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultJobShouldNotBeFound(String filter) throws Exception {
        restJobMockMvc.perform(get("/api/jobs?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingJob() throws Exception {
        // Get the job
        restJobMockMvc.perform(get("/api/jobs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateJob() throws Exception {
        // Initialize the database
        jobService.save(job);

        int databaseSizeBeforeUpdate = jobRepository.findAll().size();

        // Update the job
        Job updatedJob = jobRepository.findById(job.getId()).get();
        // Disconnect from session so that the updates on updatedJob are not directly saved in db
        em.detach(updatedJob);
        updatedJob
            .week(UPDATED_WEEK)
            .complete(UPDATED_COMPLETE)
            .paid(UPDATED_PAID);

        restJobMockMvc.perform(put("/api/jobs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedJob)))
            .andExpect(status().isOk());

        // Validate the Job in the database
        List<Job> jobList = jobRepository.findAll();
        assertThat(jobList).hasSize(databaseSizeBeforeUpdate);
        Job testJob = jobList.get(jobList.size() - 1);
        assertThat(testJob.getWeek()).isEqualTo(UPDATED_WEEK);
        assertThat(testJob.isComplete()).isEqualTo(UPDATED_COMPLETE);
        assertThat(testJob.isPaid()).isEqualTo(UPDATED_PAID);
    }

    @Test
    @Transactional
    public void updateNonExistingJob() throws Exception {
        int databaseSizeBeforeUpdate = jobRepository.findAll().size();

        // Create the Job

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restJobMockMvc.perform(put("/api/jobs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(job)))
            .andExpect(status().isBadRequest());

        // Validate the Job in the database
        List<Job> jobList = jobRepository.findAll();
        assertThat(jobList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteJob() throws Exception {
        // Initialize the database
        jobService.save(job);

        int databaseSizeBeforeDelete = jobRepository.findAll().size();

        // Get the job
        restJobMockMvc.perform(delete("/api/jobs/{id}", job.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Job> jobList = jobRepository.findAll();
        assertThat(jobList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Job.class);
        Job job1 = new Job();
        job1.setId(1L);
        Job job2 = new Job();
        job2.setId(job1.getId());
        assertThat(job1).isEqualTo(job2);
        job2.setId(2L);
        assertThat(job1).isNotEqualTo(job2);
        job1.setId(null);
        assertThat(job1).isNotEqualTo(job2);
    }
}
