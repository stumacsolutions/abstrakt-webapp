package uk.co.abstrakt.web.rest;

import uk.co.abstrakt.AbstraktApp;

import uk.co.abstrakt.domain.Customer;
import uk.co.abstrakt.domain.Job;
import uk.co.abstrakt.domain.Area;
import uk.co.abstrakt.repository.CustomerRepository;
import uk.co.abstrakt.service.CustomerService;
import uk.co.abstrakt.web.rest.errors.ExceptionTranslator;
import uk.co.abstrakt.service.dto.CustomerCriteria;
import uk.co.abstrakt.service.CustomerQueryService;

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
import java.math.BigDecimal;
import java.util.List;


import static uk.co.abstrakt.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import uk.co.abstrakt.domain.enumeration.Frequency;
import uk.co.abstrakt.domain.enumeration.PaymentMethod;
/**
 * Test class for the CustomerResource REST controller.
 *
 * @see CustomerResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AbstraktApp.class)
public class CustomerResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_PHONE = "BBBBBBBBBB";

    private static final Frequency DEFAULT_FREQUENCY = Frequency.MONTHLY;
    private static final Frequency UPDATED_FREQUENCY = Frequency.TWO_MONTHLY;

    private static final BigDecimal DEFAULT_PAYMENT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_PAYMENT_AMOUNT = new BigDecimal(2);

    private static final PaymentMethod DEFAULT_PAYMENT_METHOD = PaymentMethod.BANK;
    private static final PaymentMethod UPDATED_PAYMENT_METHOD = PaymentMethod.CASH;

    private static final String DEFAULT_FLAT_POSITION = "AAAAAAAAAA";
    private static final String UPDATED_FLAT_POSITION = "BBBBBBBBBB";

    private static final String DEFAULT_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_STREET = "AAAAAAAAAA";
    private static final String UPDATED_STREET = "BBBBBBBBBB";

    @Autowired
    private CustomerRepository customerRepository;
    
    @Autowired
    private CustomerService customerService;

    @Autowired
    private CustomerQueryService customerQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCustomerMockMvc;

    private Customer customer;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CustomerResource customerResource = new CustomerResource(customerService, customerQueryService);
        this.restCustomerMockMvc = MockMvcBuilders.standaloneSetup(customerResource)
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
    public static Customer createEntity(EntityManager em) {
        Customer customer = new Customer()
            .name(DEFAULT_NAME)
            .email(DEFAULT_EMAIL)
            .phone(DEFAULT_PHONE)
            .frequency(DEFAULT_FREQUENCY)
            .paymentAmount(DEFAULT_PAYMENT_AMOUNT)
            .paymentMethod(DEFAULT_PAYMENT_METHOD)
            .flatPosition(DEFAULT_FLAT_POSITION)
            .number(DEFAULT_NUMBER)
            .street(DEFAULT_STREET);
        // Add required entity
        Job job = JobResourceIntTest.createEntity(em);
        em.persist(job);
        em.flush();
        customer.getJobs().add(job);
        return customer;
    }

    @Before
    public void initTest() {
        customer = createEntity(em);
    }

    @Test
    @Transactional
    public void createCustomer() throws Exception {
        int databaseSizeBeforeCreate = customerRepository.findAll().size();

        // Create the Customer
        restCustomerMockMvc.perform(post("/api/customers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(customer)))
            .andExpect(status().isCreated());

        // Validate the Customer in the database
        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeCreate + 1);
        Customer testCustomer = customerList.get(customerList.size() - 1);
        assertThat(testCustomer.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCustomer.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testCustomer.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testCustomer.getFrequency()).isEqualTo(DEFAULT_FREQUENCY);
        assertThat(testCustomer.getPaymentAmount()).isEqualTo(DEFAULT_PAYMENT_AMOUNT);
        assertThat(testCustomer.getPaymentMethod()).isEqualTo(DEFAULT_PAYMENT_METHOD);
        assertThat(testCustomer.getFlatPosition()).isEqualTo(DEFAULT_FLAT_POSITION);
        assertThat(testCustomer.getNumber()).isEqualTo(DEFAULT_NUMBER);
        assertThat(testCustomer.getStreet()).isEqualTo(DEFAULT_STREET);
    }

    @Test
    @Transactional
    public void createCustomerWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = customerRepository.findAll().size();

        // Create the Customer with an existing ID
        customer.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCustomerMockMvc.perform(post("/api/customers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(customer)))
            .andExpect(status().isBadRequest());

        // Validate the Customer in the database
        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = customerRepository.findAll().size();
        // set the field null
        customer.setName(null);

        // Create the Customer, which fails.

        restCustomerMockMvc.perform(post("/api/customers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(customer)))
            .andExpect(status().isBadRequest());

        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFrequencyIsRequired() throws Exception {
        int databaseSizeBeforeTest = customerRepository.findAll().size();
        // set the field null
        customer.setFrequency(null);

        // Create the Customer, which fails.

        restCustomerMockMvc.perform(post("/api/customers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(customer)))
            .andExpect(status().isBadRequest());

        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPaymentAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = customerRepository.findAll().size();
        // set the field null
        customer.setPaymentAmount(null);

        // Create the Customer, which fails.

        restCustomerMockMvc.perform(post("/api/customers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(customer)))
            .andExpect(status().isBadRequest());

        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPaymentMethodIsRequired() throws Exception {
        int databaseSizeBeforeTest = customerRepository.findAll().size();
        // set the field null
        customer.setPaymentMethod(null);

        // Create the Customer, which fails.

        restCustomerMockMvc.perform(post("/api/customers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(customer)))
            .andExpect(status().isBadRequest());

        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = customerRepository.findAll().size();
        // set the field null
        customer.setNumber(null);

        // Create the Customer, which fails.

        restCustomerMockMvc.perform(post("/api/customers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(customer)))
            .andExpect(status().isBadRequest());

        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStreetIsRequired() throws Exception {
        int databaseSizeBeforeTest = customerRepository.findAll().size();
        // set the field null
        customer.setStreet(null);

        // Create the Customer, which fails.

        restCustomerMockMvc.perform(post("/api/customers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(customer)))
            .andExpect(status().isBadRequest());

        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCustomers() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList
        restCustomerMockMvc.perform(get("/api/customers?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(customer.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE.toString())))
            .andExpect(jsonPath("$.[*].frequency").value(hasItem(DEFAULT_FREQUENCY.toString())))
            .andExpect(jsonPath("$.[*].paymentAmount").value(hasItem(DEFAULT_PAYMENT_AMOUNT.intValue())))
            .andExpect(jsonPath("$.[*].paymentMethod").value(hasItem(DEFAULT_PAYMENT_METHOD.toString())))
            .andExpect(jsonPath("$.[*].flatPosition").value(hasItem(DEFAULT_FLAT_POSITION.toString())))
            .andExpect(jsonPath("$.[*].number").value(hasItem(DEFAULT_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].street").value(hasItem(DEFAULT_STREET.toString())));
    }
    
    @Test
    @Transactional
    public void getCustomer() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get the customer
        restCustomerMockMvc.perform(get("/api/customers/{id}", customer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(customer.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE.toString()))
            .andExpect(jsonPath("$.frequency").value(DEFAULT_FREQUENCY.toString()))
            .andExpect(jsonPath("$.paymentAmount").value(DEFAULT_PAYMENT_AMOUNT.intValue()))
            .andExpect(jsonPath("$.paymentMethod").value(DEFAULT_PAYMENT_METHOD.toString()))
            .andExpect(jsonPath("$.flatPosition").value(DEFAULT_FLAT_POSITION.toString()))
            .andExpect(jsonPath("$.number").value(DEFAULT_NUMBER.toString()))
            .andExpect(jsonPath("$.street").value(DEFAULT_STREET.toString()));
    }

    @Test
    @Transactional
    public void getAllCustomersByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where name equals to DEFAULT_NAME
        defaultCustomerShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the customerList where name equals to UPDATED_NAME
        defaultCustomerShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllCustomersByNameIsInShouldWork() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where name in DEFAULT_NAME or UPDATED_NAME
        defaultCustomerShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the customerList where name equals to UPDATED_NAME
        defaultCustomerShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllCustomersByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where name is not null
        defaultCustomerShouldBeFound("name.specified=true");

        // Get all the customerList where name is null
        defaultCustomerShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    public void getAllCustomersByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where email equals to DEFAULT_EMAIL
        defaultCustomerShouldBeFound("email.equals=" + DEFAULT_EMAIL);

        // Get all the customerList where email equals to UPDATED_EMAIL
        defaultCustomerShouldNotBeFound("email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllCustomersByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where email in DEFAULT_EMAIL or UPDATED_EMAIL
        defaultCustomerShouldBeFound("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL);

        // Get all the customerList where email equals to UPDATED_EMAIL
        defaultCustomerShouldNotBeFound("email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllCustomersByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where email is not null
        defaultCustomerShouldBeFound("email.specified=true");

        // Get all the customerList where email is null
        defaultCustomerShouldNotBeFound("email.specified=false");
    }

    @Test
    @Transactional
    public void getAllCustomersByPhoneIsEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where phone equals to DEFAULT_PHONE
        defaultCustomerShouldBeFound("phone.equals=" + DEFAULT_PHONE);

        // Get all the customerList where phone equals to UPDATED_PHONE
        defaultCustomerShouldNotBeFound("phone.equals=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    public void getAllCustomersByPhoneIsInShouldWork() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where phone in DEFAULT_PHONE or UPDATED_PHONE
        defaultCustomerShouldBeFound("phone.in=" + DEFAULT_PHONE + "," + UPDATED_PHONE);

        // Get all the customerList where phone equals to UPDATED_PHONE
        defaultCustomerShouldNotBeFound("phone.in=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    public void getAllCustomersByPhoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where phone is not null
        defaultCustomerShouldBeFound("phone.specified=true");

        // Get all the customerList where phone is null
        defaultCustomerShouldNotBeFound("phone.specified=false");
    }

    @Test
    @Transactional
    public void getAllCustomersByFrequencyIsEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where frequency equals to DEFAULT_FREQUENCY
        defaultCustomerShouldBeFound("frequency.equals=" + DEFAULT_FREQUENCY);

        // Get all the customerList where frequency equals to UPDATED_FREQUENCY
        defaultCustomerShouldNotBeFound("frequency.equals=" + UPDATED_FREQUENCY);
    }

    @Test
    @Transactional
    public void getAllCustomersByFrequencyIsInShouldWork() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where frequency in DEFAULT_FREQUENCY or UPDATED_FREQUENCY
        defaultCustomerShouldBeFound("frequency.in=" + DEFAULT_FREQUENCY + "," + UPDATED_FREQUENCY);

        // Get all the customerList where frequency equals to UPDATED_FREQUENCY
        defaultCustomerShouldNotBeFound("frequency.in=" + UPDATED_FREQUENCY);
    }

    @Test
    @Transactional
    public void getAllCustomersByFrequencyIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where frequency is not null
        defaultCustomerShouldBeFound("frequency.specified=true");

        // Get all the customerList where frequency is null
        defaultCustomerShouldNotBeFound("frequency.specified=false");
    }

    @Test
    @Transactional
    public void getAllCustomersByPaymentAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where paymentAmount equals to DEFAULT_PAYMENT_AMOUNT
        defaultCustomerShouldBeFound("paymentAmount.equals=" + DEFAULT_PAYMENT_AMOUNT);

        // Get all the customerList where paymentAmount equals to UPDATED_PAYMENT_AMOUNT
        defaultCustomerShouldNotBeFound("paymentAmount.equals=" + UPDATED_PAYMENT_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllCustomersByPaymentAmountIsInShouldWork() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where paymentAmount in DEFAULT_PAYMENT_AMOUNT or UPDATED_PAYMENT_AMOUNT
        defaultCustomerShouldBeFound("paymentAmount.in=" + DEFAULT_PAYMENT_AMOUNT + "," + UPDATED_PAYMENT_AMOUNT);

        // Get all the customerList where paymentAmount equals to UPDATED_PAYMENT_AMOUNT
        defaultCustomerShouldNotBeFound("paymentAmount.in=" + UPDATED_PAYMENT_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllCustomersByPaymentAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where paymentAmount is not null
        defaultCustomerShouldBeFound("paymentAmount.specified=true");

        // Get all the customerList where paymentAmount is null
        defaultCustomerShouldNotBeFound("paymentAmount.specified=false");
    }

    @Test
    @Transactional
    public void getAllCustomersByPaymentMethodIsEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where paymentMethod equals to DEFAULT_PAYMENT_METHOD
        defaultCustomerShouldBeFound("paymentMethod.equals=" + DEFAULT_PAYMENT_METHOD);

        // Get all the customerList where paymentMethod equals to UPDATED_PAYMENT_METHOD
        defaultCustomerShouldNotBeFound("paymentMethod.equals=" + UPDATED_PAYMENT_METHOD);
    }

    @Test
    @Transactional
    public void getAllCustomersByPaymentMethodIsInShouldWork() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where paymentMethod in DEFAULT_PAYMENT_METHOD or UPDATED_PAYMENT_METHOD
        defaultCustomerShouldBeFound("paymentMethod.in=" + DEFAULT_PAYMENT_METHOD + "," + UPDATED_PAYMENT_METHOD);

        // Get all the customerList where paymentMethod equals to UPDATED_PAYMENT_METHOD
        defaultCustomerShouldNotBeFound("paymentMethod.in=" + UPDATED_PAYMENT_METHOD);
    }

    @Test
    @Transactional
    public void getAllCustomersByPaymentMethodIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where paymentMethod is not null
        defaultCustomerShouldBeFound("paymentMethod.specified=true");

        // Get all the customerList where paymentMethod is null
        defaultCustomerShouldNotBeFound("paymentMethod.specified=false");
    }

    @Test
    @Transactional
    public void getAllCustomersByFlatPositionIsEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where flatPosition equals to DEFAULT_FLAT_POSITION
        defaultCustomerShouldBeFound("flatPosition.equals=" + DEFAULT_FLAT_POSITION);

        // Get all the customerList where flatPosition equals to UPDATED_FLAT_POSITION
        defaultCustomerShouldNotBeFound("flatPosition.equals=" + UPDATED_FLAT_POSITION);
    }

    @Test
    @Transactional
    public void getAllCustomersByFlatPositionIsInShouldWork() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where flatPosition in DEFAULT_FLAT_POSITION or UPDATED_FLAT_POSITION
        defaultCustomerShouldBeFound("flatPosition.in=" + DEFAULT_FLAT_POSITION + "," + UPDATED_FLAT_POSITION);

        // Get all the customerList where flatPosition equals to UPDATED_FLAT_POSITION
        defaultCustomerShouldNotBeFound("flatPosition.in=" + UPDATED_FLAT_POSITION);
    }

    @Test
    @Transactional
    public void getAllCustomersByFlatPositionIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where flatPosition is not null
        defaultCustomerShouldBeFound("flatPosition.specified=true");

        // Get all the customerList where flatPosition is null
        defaultCustomerShouldNotBeFound("flatPosition.specified=false");
    }

    @Test
    @Transactional
    public void getAllCustomersByNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where number equals to DEFAULT_NUMBER
        defaultCustomerShouldBeFound("number.equals=" + DEFAULT_NUMBER);

        // Get all the customerList where number equals to UPDATED_NUMBER
        defaultCustomerShouldNotBeFound("number.equals=" + UPDATED_NUMBER);
    }

    @Test
    @Transactional
    public void getAllCustomersByNumberIsInShouldWork() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where number in DEFAULT_NUMBER or UPDATED_NUMBER
        defaultCustomerShouldBeFound("number.in=" + DEFAULT_NUMBER + "," + UPDATED_NUMBER);

        // Get all the customerList where number equals to UPDATED_NUMBER
        defaultCustomerShouldNotBeFound("number.in=" + UPDATED_NUMBER);
    }

    @Test
    @Transactional
    public void getAllCustomersByNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where number is not null
        defaultCustomerShouldBeFound("number.specified=true");

        // Get all the customerList where number is null
        defaultCustomerShouldNotBeFound("number.specified=false");
    }

    @Test
    @Transactional
    public void getAllCustomersByStreetIsEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where street equals to DEFAULT_STREET
        defaultCustomerShouldBeFound("street.equals=" + DEFAULT_STREET);

        // Get all the customerList where street equals to UPDATED_STREET
        defaultCustomerShouldNotBeFound("street.equals=" + UPDATED_STREET);
    }

    @Test
    @Transactional
    public void getAllCustomersByStreetIsInShouldWork() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where street in DEFAULT_STREET or UPDATED_STREET
        defaultCustomerShouldBeFound("street.in=" + DEFAULT_STREET + "," + UPDATED_STREET);

        // Get all the customerList where street equals to UPDATED_STREET
        defaultCustomerShouldNotBeFound("street.in=" + UPDATED_STREET);
    }

    @Test
    @Transactional
    public void getAllCustomersByStreetIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where street is not null
        defaultCustomerShouldBeFound("street.specified=true");

        // Get all the customerList where street is null
        defaultCustomerShouldNotBeFound("street.specified=false");
    }

    @Test
    @Transactional
    public void getAllCustomersByJobsIsEqualToSomething() throws Exception {
        // Initialize the database
        Job jobs = JobResourceIntTest.createEntity(em);
        em.persist(jobs);
        em.flush();
        customer.addJobs(jobs);
        customerRepository.saveAndFlush(customer);
        Long jobsId = jobs.getId();

        // Get all the customerList where jobs equals to jobsId
        defaultCustomerShouldBeFound("jobsId.equals=" + jobsId);

        // Get all the customerList where jobs equals to jobsId + 1
        defaultCustomerShouldNotBeFound("jobsId.equals=" + (jobsId + 1));
    }


    @Test
    @Transactional
    public void getAllCustomersByAreaIsEqualToSomething() throws Exception {
        // Initialize the database
        Area area = AreaResourceIntTest.createEntity(em);
        em.persist(area);
        em.flush();
        customer.setArea(area);
        customerRepository.saveAndFlush(customer);
        Long areaId = area.getId();

        // Get all the customerList where area equals to areaId
        defaultCustomerShouldBeFound("areaId.equals=" + areaId);

        // Get all the customerList where area equals to areaId + 1
        defaultCustomerShouldNotBeFound("areaId.equals=" + (areaId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultCustomerShouldBeFound(String filter) throws Exception {
        restCustomerMockMvc.perform(get("/api/customers?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(customer.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE.toString())))
            .andExpect(jsonPath("$.[*].frequency").value(hasItem(DEFAULT_FREQUENCY.toString())))
            .andExpect(jsonPath("$.[*].paymentAmount").value(hasItem(DEFAULT_PAYMENT_AMOUNT.intValue())))
            .andExpect(jsonPath("$.[*].paymentMethod").value(hasItem(DEFAULT_PAYMENT_METHOD.toString())))
            .andExpect(jsonPath("$.[*].flatPosition").value(hasItem(DEFAULT_FLAT_POSITION.toString())))
            .andExpect(jsonPath("$.[*].number").value(hasItem(DEFAULT_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].street").value(hasItem(DEFAULT_STREET.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultCustomerShouldNotBeFound(String filter) throws Exception {
        restCustomerMockMvc.perform(get("/api/customers?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingCustomer() throws Exception {
        // Get the customer
        restCustomerMockMvc.perform(get("/api/customers/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCustomer() throws Exception {
        // Initialize the database
        customerService.save(customer);

        int databaseSizeBeforeUpdate = customerRepository.findAll().size();

        // Update the customer
        Customer updatedCustomer = customerRepository.findById(customer.getId()).get();
        // Disconnect from session so that the updates on updatedCustomer are not directly saved in db
        em.detach(updatedCustomer);
        updatedCustomer
            .name(UPDATED_NAME)
            .email(UPDATED_EMAIL)
            .phone(UPDATED_PHONE)
            .frequency(UPDATED_FREQUENCY)
            .paymentAmount(UPDATED_PAYMENT_AMOUNT)
            .paymentMethod(UPDATED_PAYMENT_METHOD)
            .flatPosition(UPDATED_FLAT_POSITION)
            .number(UPDATED_NUMBER)
            .street(UPDATED_STREET);

        restCustomerMockMvc.perform(put("/api/customers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCustomer)))
            .andExpect(status().isOk());

        // Validate the Customer in the database
        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeUpdate);
        Customer testCustomer = customerList.get(customerList.size() - 1);
        assertThat(testCustomer.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCustomer.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testCustomer.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testCustomer.getFrequency()).isEqualTo(UPDATED_FREQUENCY);
        assertThat(testCustomer.getPaymentAmount()).isEqualTo(UPDATED_PAYMENT_AMOUNT);
        assertThat(testCustomer.getPaymentMethod()).isEqualTo(UPDATED_PAYMENT_METHOD);
        assertThat(testCustomer.getFlatPosition()).isEqualTo(UPDATED_FLAT_POSITION);
        assertThat(testCustomer.getNumber()).isEqualTo(UPDATED_NUMBER);
        assertThat(testCustomer.getStreet()).isEqualTo(UPDATED_STREET);
    }

    @Test
    @Transactional
    public void updateNonExistingCustomer() throws Exception {
        int databaseSizeBeforeUpdate = customerRepository.findAll().size();

        // Create the Customer

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCustomerMockMvc.perform(put("/api/customers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(customer)))
            .andExpect(status().isBadRequest());

        // Validate the Customer in the database
        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteCustomer() throws Exception {
        // Initialize the database
        customerService.save(customer);

        int databaseSizeBeforeDelete = customerRepository.findAll().size();

        // Get the customer
        restCustomerMockMvc.perform(delete("/api/customers/{id}", customer.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Customer.class);
        Customer customer1 = new Customer();
        customer1.setId(1L);
        Customer customer2 = new Customer();
        customer2.setId(customer1.getId());
        assertThat(customer1).isEqualTo(customer2);
        customer2.setId(2L);
        assertThat(customer1).isNotEqualTo(customer2);
        customer1.setId(null);
        assertThat(customer1).isNotEqualTo(customer2);
    }
}
