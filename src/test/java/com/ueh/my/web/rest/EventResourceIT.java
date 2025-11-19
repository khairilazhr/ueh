package com.ueh.my.web.rest;

import static com.ueh.my.domain.EventAsserts.*;
import static com.ueh.my.web.rest.TestUtil.createUpdateProxyForBean;
import static com.ueh.my.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ueh.my.IntegrationTest;
import com.ueh.my.domain.Event;
import com.ueh.my.repository.EventRepository;
import com.ueh.my.service.dto.EventDTO;
import com.ueh.my.service.mapper.EventMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Base64;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link EventResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EventResourceIT {

    private static final String DEFAULT_EVENT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_EVENT_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_EVENT_DESC = "AAAAAAAAAA";
    private static final String UPDATED_EVENT_DESC = "BBBBBBBBBB";

    private static final String DEFAULT_EVENT_ORG = "AAAAAAAAAA";
    private static final String UPDATED_EVENT_ORG = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_EVENT_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_EVENT_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_EVENT_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final String DEFAULT_EVENT_LOCATION = "AAAAAAAAAA";
    private static final String UPDATED_EVENT_LOCATION = "BBBBBBBBBB";

    private static final byte[] DEFAULT_EVENT_POSTER = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_EVENT_POSTER = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_EVENT_POSTER_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_EVENT_POSTER_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_ENTERED_BY = "AAAAAAAAAA";
    private static final String UPDATED_ENTERED_BY = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_ENTERED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_ENTERED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_ENTERED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final String DEFAULT_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_MODIFIED_BY = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_MODIFIED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_MODIFIED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_MODIFIED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final String DEFAULT_EVENT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_EVENT_STATUS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/events";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private EventMapper eventMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEventMockMvc;

    private Event event;

    private Event insertedEvent;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Event createEntity() {
        return new Event()
            .eventName(DEFAULT_EVENT_NAME)
            .eventDesc(DEFAULT_EVENT_DESC)
            .eventOrg(DEFAULT_EVENT_ORG)
            .eventDate(DEFAULT_EVENT_DATE)
            .eventLocation(DEFAULT_EVENT_LOCATION)
            .eventPoster(DEFAULT_EVENT_POSTER)
            .eventPosterContentType(DEFAULT_EVENT_POSTER_CONTENT_TYPE)
            .enteredBy(DEFAULT_ENTERED_BY)
            .enteredDate(DEFAULT_ENTERED_DATE)
            .modifiedBy(DEFAULT_MODIFIED_BY)
            .modifiedDate(DEFAULT_MODIFIED_DATE)
            .eventStatus(DEFAULT_EVENT_STATUS);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Event createUpdatedEntity() {
        return new Event()
            .eventName(UPDATED_EVENT_NAME)
            .eventDesc(UPDATED_EVENT_DESC)
            .eventOrg(UPDATED_EVENT_ORG)
            .eventDate(UPDATED_EVENT_DATE)
            .eventLocation(UPDATED_EVENT_LOCATION)
            .eventPoster(UPDATED_EVENT_POSTER)
            .eventPosterContentType(UPDATED_EVENT_POSTER_CONTENT_TYPE)
            .enteredBy(UPDATED_ENTERED_BY)
            .enteredDate(UPDATED_ENTERED_DATE)
            .modifiedBy(UPDATED_MODIFIED_BY)
            .modifiedDate(UPDATED_MODIFIED_DATE)
            .eventStatus(UPDATED_EVENT_STATUS);
    }

    @BeforeEach
    void initTest() {
        event = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedEvent != null) {
            eventRepository.delete(insertedEvent);
            insertedEvent = null;
        }
    }

    @Test
    @Transactional
    void createEvent() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Event
        EventDTO eventDTO = eventMapper.toDto(event);
        var returnedEventDTO = om.readValue(
            restEventMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(eventDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            EventDTO.class
        );

        // Validate the Event in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedEvent = eventMapper.toEntity(returnedEventDTO);
        assertEventUpdatableFieldsEquals(returnedEvent, getPersistedEvent(returnedEvent));

        insertedEvent = returnedEvent;
    }

    @Test
    @Transactional
    void createEventWithExistingId() throws Exception {
        // Create the Event with an existing ID
        event.setId(1L);
        EventDTO eventDTO = eventMapper.toDto(event);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEventMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(eventDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Event in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkEventNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        event.setEventName(null);

        // Create the Event, which fails.
        EventDTO eventDTO = eventMapper.toDto(event);

        restEventMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(eventDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEventDescIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        event.setEventDesc(null);

        // Create the Event, which fails.
        EventDTO eventDTO = eventMapper.toDto(event);

        restEventMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(eventDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEventOrgIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        event.setEventOrg(null);

        // Create the Event, which fails.
        EventDTO eventDTO = eventMapper.toDto(event);

        restEventMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(eventDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEventDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        event.setEventDate(null);

        // Create the Event, which fails.
        EventDTO eventDTO = eventMapper.toDto(event);

        restEventMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(eventDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllEvents() throws Exception {
        // Initialize the database
        insertedEvent = eventRepository.saveAndFlush(event);

        // Get all the eventList
        restEventMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(event.getId().intValue())))
            .andExpect(jsonPath("$.[*].eventName").value(hasItem(DEFAULT_EVENT_NAME)))
            .andExpect(jsonPath("$.[*].eventDesc").value(hasItem(DEFAULT_EVENT_DESC)))
            .andExpect(jsonPath("$.[*].eventOrg").value(hasItem(DEFAULT_EVENT_ORG)))
            .andExpect(jsonPath("$.[*].eventDate").value(hasItem(sameInstant(DEFAULT_EVENT_DATE))))
            .andExpect(jsonPath("$.[*].eventLocation").value(hasItem(DEFAULT_EVENT_LOCATION)))
            .andExpect(jsonPath("$.[*].eventPosterContentType").value(hasItem(DEFAULT_EVENT_POSTER_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].eventPoster").value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_EVENT_POSTER))))
            .andExpect(jsonPath("$.[*].enteredBy").value(hasItem(DEFAULT_ENTERED_BY)))
            .andExpect(jsonPath("$.[*].enteredDate").value(hasItem(sameInstant(DEFAULT_ENTERED_DATE))))
            .andExpect(jsonPath("$.[*].modifiedBy").value(hasItem(DEFAULT_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].modifiedDate").value(hasItem(sameInstant(DEFAULT_MODIFIED_DATE))))
            .andExpect(jsonPath("$.[*].eventStatus").value(hasItem(DEFAULT_EVENT_STATUS)));
    }

    @Test
    @Transactional
    void getEvent() throws Exception {
        // Initialize the database
        insertedEvent = eventRepository.saveAndFlush(event);

        // Get the event
        restEventMockMvc
            .perform(get(ENTITY_API_URL_ID, event.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(event.getId().intValue()))
            .andExpect(jsonPath("$.eventName").value(DEFAULT_EVENT_NAME))
            .andExpect(jsonPath("$.eventDesc").value(DEFAULT_EVENT_DESC))
            .andExpect(jsonPath("$.eventOrg").value(DEFAULT_EVENT_ORG))
            .andExpect(jsonPath("$.eventDate").value(sameInstant(DEFAULT_EVENT_DATE)))
            .andExpect(jsonPath("$.eventLocation").value(DEFAULT_EVENT_LOCATION))
            .andExpect(jsonPath("$.eventPosterContentType").value(DEFAULT_EVENT_POSTER_CONTENT_TYPE))
            .andExpect(jsonPath("$.eventPoster").value(Base64.getEncoder().encodeToString(DEFAULT_EVENT_POSTER)))
            .andExpect(jsonPath("$.enteredBy").value(DEFAULT_ENTERED_BY))
            .andExpect(jsonPath("$.enteredDate").value(sameInstant(DEFAULT_ENTERED_DATE)))
            .andExpect(jsonPath("$.modifiedBy").value(DEFAULT_MODIFIED_BY))
            .andExpect(jsonPath("$.modifiedDate").value(sameInstant(DEFAULT_MODIFIED_DATE)))
            .andExpect(jsonPath("$.eventStatus").value(DEFAULT_EVENT_STATUS));
    }

    @Test
    @Transactional
    void getEventsByIdFiltering() throws Exception {
        // Initialize the database
        insertedEvent = eventRepository.saveAndFlush(event);

        Long id = event.getId();

        defaultEventFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultEventFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultEventFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllEventsByEventNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEvent = eventRepository.saveAndFlush(event);

        // Get all the eventList where eventName equals to
        defaultEventFiltering("eventName.equals=" + DEFAULT_EVENT_NAME, "eventName.equals=" + UPDATED_EVENT_NAME);
    }

    @Test
    @Transactional
    void getAllEventsByEventNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEvent = eventRepository.saveAndFlush(event);

        // Get all the eventList where eventName in
        defaultEventFiltering("eventName.in=" + DEFAULT_EVENT_NAME + "," + UPDATED_EVENT_NAME, "eventName.in=" + UPDATED_EVENT_NAME);
    }

    @Test
    @Transactional
    void getAllEventsByEventNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEvent = eventRepository.saveAndFlush(event);

        // Get all the eventList where eventName is not null
        defaultEventFiltering("eventName.specified=true", "eventName.specified=false");
    }

    @Test
    @Transactional
    void getAllEventsByEventNameContainsSomething() throws Exception {
        // Initialize the database
        insertedEvent = eventRepository.saveAndFlush(event);

        // Get all the eventList where eventName contains
        defaultEventFiltering("eventName.contains=" + DEFAULT_EVENT_NAME, "eventName.contains=" + UPDATED_EVENT_NAME);
    }

    @Test
    @Transactional
    void getAllEventsByEventNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedEvent = eventRepository.saveAndFlush(event);

        // Get all the eventList where eventName does not contain
        defaultEventFiltering("eventName.doesNotContain=" + UPDATED_EVENT_NAME, "eventName.doesNotContain=" + DEFAULT_EVENT_NAME);
    }

    @Test
    @Transactional
    void getAllEventsByEventDescIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEvent = eventRepository.saveAndFlush(event);

        // Get all the eventList where eventDesc equals to
        defaultEventFiltering("eventDesc.equals=" + DEFAULT_EVENT_DESC, "eventDesc.equals=" + UPDATED_EVENT_DESC);
    }

    @Test
    @Transactional
    void getAllEventsByEventDescIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEvent = eventRepository.saveAndFlush(event);

        // Get all the eventList where eventDesc in
        defaultEventFiltering("eventDesc.in=" + DEFAULT_EVENT_DESC + "," + UPDATED_EVENT_DESC, "eventDesc.in=" + UPDATED_EVENT_DESC);
    }

    @Test
    @Transactional
    void getAllEventsByEventDescIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEvent = eventRepository.saveAndFlush(event);

        // Get all the eventList where eventDesc is not null
        defaultEventFiltering("eventDesc.specified=true", "eventDesc.specified=false");
    }

    @Test
    @Transactional
    void getAllEventsByEventDescContainsSomething() throws Exception {
        // Initialize the database
        insertedEvent = eventRepository.saveAndFlush(event);

        // Get all the eventList where eventDesc contains
        defaultEventFiltering("eventDesc.contains=" + DEFAULT_EVENT_DESC, "eventDesc.contains=" + UPDATED_EVENT_DESC);
    }

    @Test
    @Transactional
    void getAllEventsByEventDescNotContainsSomething() throws Exception {
        // Initialize the database
        insertedEvent = eventRepository.saveAndFlush(event);

        // Get all the eventList where eventDesc does not contain
        defaultEventFiltering("eventDesc.doesNotContain=" + UPDATED_EVENT_DESC, "eventDesc.doesNotContain=" + DEFAULT_EVENT_DESC);
    }

    @Test
    @Transactional
    void getAllEventsByEventOrgIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEvent = eventRepository.saveAndFlush(event);

        // Get all the eventList where eventOrg equals to
        defaultEventFiltering("eventOrg.equals=" + DEFAULT_EVENT_ORG, "eventOrg.equals=" + UPDATED_EVENT_ORG);
    }

    @Test
    @Transactional
    void getAllEventsByEventOrgIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEvent = eventRepository.saveAndFlush(event);

        // Get all the eventList where eventOrg in
        defaultEventFiltering("eventOrg.in=" + DEFAULT_EVENT_ORG + "," + UPDATED_EVENT_ORG, "eventOrg.in=" + UPDATED_EVENT_ORG);
    }

    @Test
    @Transactional
    void getAllEventsByEventOrgIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEvent = eventRepository.saveAndFlush(event);

        // Get all the eventList where eventOrg is not null
        defaultEventFiltering("eventOrg.specified=true", "eventOrg.specified=false");
    }

    @Test
    @Transactional
    void getAllEventsByEventOrgContainsSomething() throws Exception {
        // Initialize the database
        insertedEvent = eventRepository.saveAndFlush(event);

        // Get all the eventList where eventOrg contains
        defaultEventFiltering("eventOrg.contains=" + DEFAULT_EVENT_ORG, "eventOrg.contains=" + UPDATED_EVENT_ORG);
    }

    @Test
    @Transactional
    void getAllEventsByEventOrgNotContainsSomething() throws Exception {
        // Initialize the database
        insertedEvent = eventRepository.saveAndFlush(event);

        // Get all the eventList where eventOrg does not contain
        defaultEventFiltering("eventOrg.doesNotContain=" + UPDATED_EVENT_ORG, "eventOrg.doesNotContain=" + DEFAULT_EVENT_ORG);
    }

    @Test
    @Transactional
    void getAllEventsByEventDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEvent = eventRepository.saveAndFlush(event);

        // Get all the eventList where eventDate equals to
        defaultEventFiltering("eventDate.equals=" + DEFAULT_EVENT_DATE, "eventDate.equals=" + UPDATED_EVENT_DATE);
    }

    @Test
    @Transactional
    void getAllEventsByEventDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEvent = eventRepository.saveAndFlush(event);

        // Get all the eventList where eventDate in
        defaultEventFiltering("eventDate.in=" + DEFAULT_EVENT_DATE + "," + UPDATED_EVENT_DATE, "eventDate.in=" + UPDATED_EVENT_DATE);
    }

    @Test
    @Transactional
    void getAllEventsByEventDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEvent = eventRepository.saveAndFlush(event);

        // Get all the eventList where eventDate is not null
        defaultEventFiltering("eventDate.specified=true", "eventDate.specified=false");
    }

    @Test
    @Transactional
    void getAllEventsByEventDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedEvent = eventRepository.saveAndFlush(event);

        // Get all the eventList where eventDate is greater than or equal to
        defaultEventFiltering("eventDate.greaterThanOrEqual=" + DEFAULT_EVENT_DATE, "eventDate.greaterThanOrEqual=" + UPDATED_EVENT_DATE);
    }

    @Test
    @Transactional
    void getAllEventsByEventDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedEvent = eventRepository.saveAndFlush(event);

        // Get all the eventList where eventDate is less than or equal to
        defaultEventFiltering("eventDate.lessThanOrEqual=" + DEFAULT_EVENT_DATE, "eventDate.lessThanOrEqual=" + SMALLER_EVENT_DATE);
    }

    @Test
    @Transactional
    void getAllEventsByEventDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedEvent = eventRepository.saveAndFlush(event);

        // Get all the eventList where eventDate is less than
        defaultEventFiltering("eventDate.lessThan=" + UPDATED_EVENT_DATE, "eventDate.lessThan=" + DEFAULT_EVENT_DATE);
    }

    @Test
    @Transactional
    void getAllEventsByEventDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedEvent = eventRepository.saveAndFlush(event);

        // Get all the eventList where eventDate is greater than
        defaultEventFiltering("eventDate.greaterThan=" + SMALLER_EVENT_DATE, "eventDate.greaterThan=" + DEFAULT_EVENT_DATE);
    }

    @Test
    @Transactional
    void getAllEventsByEventLocationIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEvent = eventRepository.saveAndFlush(event);

        // Get all the eventList where eventLocation equals to
        defaultEventFiltering("eventLocation.equals=" + DEFAULT_EVENT_LOCATION, "eventLocation.equals=" + UPDATED_EVENT_LOCATION);
    }

    @Test
    @Transactional
    void getAllEventsByEventLocationIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEvent = eventRepository.saveAndFlush(event);

        // Get all the eventList where eventLocation in
        defaultEventFiltering(
            "eventLocation.in=" + DEFAULT_EVENT_LOCATION + "," + UPDATED_EVENT_LOCATION,
            "eventLocation.in=" + UPDATED_EVENT_LOCATION
        );
    }

    @Test
    @Transactional
    void getAllEventsByEventLocationIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEvent = eventRepository.saveAndFlush(event);

        // Get all the eventList where eventLocation is not null
        defaultEventFiltering("eventLocation.specified=true", "eventLocation.specified=false");
    }

    @Test
    @Transactional
    void getAllEventsByEventLocationContainsSomething() throws Exception {
        // Initialize the database
        insertedEvent = eventRepository.saveAndFlush(event);

        // Get all the eventList where eventLocation contains
        defaultEventFiltering("eventLocation.contains=" + DEFAULT_EVENT_LOCATION, "eventLocation.contains=" + UPDATED_EVENT_LOCATION);
    }

    @Test
    @Transactional
    void getAllEventsByEventLocationNotContainsSomething() throws Exception {
        // Initialize the database
        insertedEvent = eventRepository.saveAndFlush(event);

        // Get all the eventList where eventLocation does not contain
        defaultEventFiltering(
            "eventLocation.doesNotContain=" + UPDATED_EVENT_LOCATION,
            "eventLocation.doesNotContain=" + DEFAULT_EVENT_LOCATION
        );
    }

    @Test
    @Transactional
    void getAllEventsByEnteredByIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEvent = eventRepository.saveAndFlush(event);

        // Get all the eventList where enteredBy equals to
        defaultEventFiltering("enteredBy.equals=" + DEFAULT_ENTERED_BY, "enteredBy.equals=" + UPDATED_ENTERED_BY);
    }

    @Test
    @Transactional
    void getAllEventsByEnteredByIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEvent = eventRepository.saveAndFlush(event);

        // Get all the eventList where enteredBy in
        defaultEventFiltering("enteredBy.in=" + DEFAULT_ENTERED_BY + "," + UPDATED_ENTERED_BY, "enteredBy.in=" + UPDATED_ENTERED_BY);
    }

    @Test
    @Transactional
    void getAllEventsByEnteredByIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEvent = eventRepository.saveAndFlush(event);

        // Get all the eventList where enteredBy is not null
        defaultEventFiltering("enteredBy.specified=true", "enteredBy.specified=false");
    }

    @Test
    @Transactional
    void getAllEventsByEnteredByContainsSomething() throws Exception {
        // Initialize the database
        insertedEvent = eventRepository.saveAndFlush(event);

        // Get all the eventList where enteredBy contains
        defaultEventFiltering("enteredBy.contains=" + DEFAULT_ENTERED_BY, "enteredBy.contains=" + UPDATED_ENTERED_BY);
    }

    @Test
    @Transactional
    void getAllEventsByEnteredByNotContainsSomething() throws Exception {
        // Initialize the database
        insertedEvent = eventRepository.saveAndFlush(event);

        // Get all the eventList where enteredBy does not contain
        defaultEventFiltering("enteredBy.doesNotContain=" + UPDATED_ENTERED_BY, "enteredBy.doesNotContain=" + DEFAULT_ENTERED_BY);
    }

    @Test
    @Transactional
    void getAllEventsByEnteredDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEvent = eventRepository.saveAndFlush(event);

        // Get all the eventList where enteredDate equals to
        defaultEventFiltering("enteredDate.equals=" + DEFAULT_ENTERED_DATE, "enteredDate.equals=" + UPDATED_ENTERED_DATE);
    }

    @Test
    @Transactional
    void getAllEventsByEnteredDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEvent = eventRepository.saveAndFlush(event);

        // Get all the eventList where enteredDate in
        defaultEventFiltering(
            "enteredDate.in=" + DEFAULT_ENTERED_DATE + "," + UPDATED_ENTERED_DATE,
            "enteredDate.in=" + UPDATED_ENTERED_DATE
        );
    }

    @Test
    @Transactional
    void getAllEventsByEnteredDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEvent = eventRepository.saveAndFlush(event);

        // Get all the eventList where enteredDate is not null
        defaultEventFiltering("enteredDate.specified=true", "enteredDate.specified=false");
    }

    @Test
    @Transactional
    void getAllEventsByEnteredDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedEvent = eventRepository.saveAndFlush(event);

        // Get all the eventList where enteredDate is greater than or equal to
        defaultEventFiltering(
            "enteredDate.greaterThanOrEqual=" + DEFAULT_ENTERED_DATE,
            "enteredDate.greaterThanOrEqual=" + UPDATED_ENTERED_DATE
        );
    }

    @Test
    @Transactional
    void getAllEventsByEnteredDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedEvent = eventRepository.saveAndFlush(event);

        // Get all the eventList where enteredDate is less than or equal to
        defaultEventFiltering("enteredDate.lessThanOrEqual=" + DEFAULT_ENTERED_DATE, "enteredDate.lessThanOrEqual=" + SMALLER_ENTERED_DATE);
    }

    @Test
    @Transactional
    void getAllEventsByEnteredDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedEvent = eventRepository.saveAndFlush(event);

        // Get all the eventList where enteredDate is less than
        defaultEventFiltering("enteredDate.lessThan=" + UPDATED_ENTERED_DATE, "enteredDate.lessThan=" + DEFAULT_ENTERED_DATE);
    }

    @Test
    @Transactional
    void getAllEventsByEnteredDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedEvent = eventRepository.saveAndFlush(event);

        // Get all the eventList where enteredDate is greater than
        defaultEventFiltering("enteredDate.greaterThan=" + SMALLER_ENTERED_DATE, "enteredDate.greaterThan=" + DEFAULT_ENTERED_DATE);
    }

    @Test
    @Transactional
    void getAllEventsByModifiedByIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEvent = eventRepository.saveAndFlush(event);

        // Get all the eventList where modifiedBy equals to
        defaultEventFiltering("modifiedBy.equals=" + DEFAULT_MODIFIED_BY, "modifiedBy.equals=" + UPDATED_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllEventsByModifiedByIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEvent = eventRepository.saveAndFlush(event);

        // Get all the eventList where modifiedBy in
        defaultEventFiltering("modifiedBy.in=" + DEFAULT_MODIFIED_BY + "," + UPDATED_MODIFIED_BY, "modifiedBy.in=" + UPDATED_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllEventsByModifiedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEvent = eventRepository.saveAndFlush(event);

        // Get all the eventList where modifiedBy is not null
        defaultEventFiltering("modifiedBy.specified=true", "modifiedBy.specified=false");
    }

    @Test
    @Transactional
    void getAllEventsByModifiedByContainsSomething() throws Exception {
        // Initialize the database
        insertedEvent = eventRepository.saveAndFlush(event);

        // Get all the eventList where modifiedBy contains
        defaultEventFiltering("modifiedBy.contains=" + DEFAULT_MODIFIED_BY, "modifiedBy.contains=" + UPDATED_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllEventsByModifiedByNotContainsSomething() throws Exception {
        // Initialize the database
        insertedEvent = eventRepository.saveAndFlush(event);

        // Get all the eventList where modifiedBy does not contain
        defaultEventFiltering("modifiedBy.doesNotContain=" + UPDATED_MODIFIED_BY, "modifiedBy.doesNotContain=" + DEFAULT_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllEventsByModifiedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEvent = eventRepository.saveAndFlush(event);

        // Get all the eventList where modifiedDate equals to
        defaultEventFiltering("modifiedDate.equals=" + DEFAULT_MODIFIED_DATE, "modifiedDate.equals=" + UPDATED_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllEventsByModifiedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEvent = eventRepository.saveAndFlush(event);

        // Get all the eventList where modifiedDate in
        defaultEventFiltering(
            "modifiedDate.in=" + DEFAULT_MODIFIED_DATE + "," + UPDATED_MODIFIED_DATE,
            "modifiedDate.in=" + UPDATED_MODIFIED_DATE
        );
    }

    @Test
    @Transactional
    void getAllEventsByModifiedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEvent = eventRepository.saveAndFlush(event);

        // Get all the eventList where modifiedDate is not null
        defaultEventFiltering("modifiedDate.specified=true", "modifiedDate.specified=false");
    }

    @Test
    @Transactional
    void getAllEventsByModifiedDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedEvent = eventRepository.saveAndFlush(event);

        // Get all the eventList where modifiedDate is greater than or equal to
        defaultEventFiltering(
            "modifiedDate.greaterThanOrEqual=" + DEFAULT_MODIFIED_DATE,
            "modifiedDate.greaterThanOrEqual=" + UPDATED_MODIFIED_DATE
        );
    }

    @Test
    @Transactional
    void getAllEventsByModifiedDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedEvent = eventRepository.saveAndFlush(event);

        // Get all the eventList where modifiedDate is less than or equal to
        defaultEventFiltering(
            "modifiedDate.lessThanOrEqual=" + DEFAULT_MODIFIED_DATE,
            "modifiedDate.lessThanOrEqual=" + SMALLER_MODIFIED_DATE
        );
    }

    @Test
    @Transactional
    void getAllEventsByModifiedDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedEvent = eventRepository.saveAndFlush(event);

        // Get all the eventList where modifiedDate is less than
        defaultEventFiltering("modifiedDate.lessThan=" + UPDATED_MODIFIED_DATE, "modifiedDate.lessThan=" + DEFAULT_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllEventsByModifiedDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedEvent = eventRepository.saveAndFlush(event);

        // Get all the eventList where modifiedDate is greater than
        defaultEventFiltering("modifiedDate.greaterThan=" + SMALLER_MODIFIED_DATE, "modifiedDate.greaterThan=" + DEFAULT_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllEventsByEventStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEvent = eventRepository.saveAndFlush(event);

        // Get all the eventList where eventStatus equals to
        defaultEventFiltering("eventStatus.equals=" + DEFAULT_EVENT_STATUS, "eventStatus.equals=" + UPDATED_EVENT_STATUS);
    }

    @Test
    @Transactional
    void getAllEventsByEventStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEvent = eventRepository.saveAndFlush(event);

        // Get all the eventList where eventStatus in
        defaultEventFiltering(
            "eventStatus.in=" + DEFAULT_EVENT_STATUS + "," + UPDATED_EVENT_STATUS,
            "eventStatus.in=" + UPDATED_EVENT_STATUS
        );
    }

    @Test
    @Transactional
    void getAllEventsByEventStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEvent = eventRepository.saveAndFlush(event);

        // Get all the eventList where eventStatus is not null
        defaultEventFiltering("eventStatus.specified=true", "eventStatus.specified=false");
    }

    @Test
    @Transactional
    void getAllEventsByEventStatusContainsSomething() throws Exception {
        // Initialize the database
        insertedEvent = eventRepository.saveAndFlush(event);

        // Get all the eventList where eventStatus contains
        defaultEventFiltering("eventStatus.contains=" + DEFAULT_EVENT_STATUS, "eventStatus.contains=" + UPDATED_EVENT_STATUS);
    }

    @Test
    @Transactional
    void getAllEventsByEventStatusNotContainsSomething() throws Exception {
        // Initialize the database
        insertedEvent = eventRepository.saveAndFlush(event);

        // Get all the eventList where eventStatus does not contain
        defaultEventFiltering("eventStatus.doesNotContain=" + UPDATED_EVENT_STATUS, "eventStatus.doesNotContain=" + DEFAULT_EVENT_STATUS);
    }

    private void defaultEventFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultEventShouldBeFound(shouldBeFound);
        defaultEventShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultEventShouldBeFound(String filter) throws Exception {
        restEventMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(event.getId().intValue())))
            .andExpect(jsonPath("$.[*].eventName").value(hasItem(DEFAULT_EVENT_NAME)))
            .andExpect(jsonPath("$.[*].eventDesc").value(hasItem(DEFAULT_EVENT_DESC)))
            .andExpect(jsonPath("$.[*].eventOrg").value(hasItem(DEFAULT_EVENT_ORG)))
            .andExpect(jsonPath("$.[*].eventDate").value(hasItem(sameInstant(DEFAULT_EVENT_DATE))))
            .andExpect(jsonPath("$.[*].eventLocation").value(hasItem(DEFAULT_EVENT_LOCATION)))
            .andExpect(jsonPath("$.[*].eventPosterContentType").value(hasItem(DEFAULT_EVENT_POSTER_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].eventPoster").value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_EVENT_POSTER))))
            .andExpect(jsonPath("$.[*].enteredBy").value(hasItem(DEFAULT_ENTERED_BY)))
            .andExpect(jsonPath("$.[*].enteredDate").value(hasItem(sameInstant(DEFAULT_ENTERED_DATE))))
            .andExpect(jsonPath("$.[*].modifiedBy").value(hasItem(DEFAULT_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].modifiedDate").value(hasItem(sameInstant(DEFAULT_MODIFIED_DATE))))
            .andExpect(jsonPath("$.[*].eventStatus").value(hasItem(DEFAULT_EVENT_STATUS)));

        // Check, that the count call also returns 1
        restEventMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultEventShouldNotBeFound(String filter) throws Exception {
        restEventMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restEventMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingEvent() throws Exception {
        // Get the event
        restEventMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEvent() throws Exception {
        // Initialize the database
        insertedEvent = eventRepository.saveAndFlush(event);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the event
        Event updatedEvent = eventRepository.findById(event.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedEvent are not directly saved in db
        em.detach(updatedEvent);
        updatedEvent
            .eventName(UPDATED_EVENT_NAME)
            .eventDesc(UPDATED_EVENT_DESC)
            .eventOrg(UPDATED_EVENT_ORG)
            .eventDate(UPDATED_EVENT_DATE)
            .eventLocation(UPDATED_EVENT_LOCATION)
            .eventPoster(UPDATED_EVENT_POSTER)
            .eventPosterContentType(UPDATED_EVENT_POSTER_CONTENT_TYPE)
            .enteredBy(UPDATED_ENTERED_BY)
            .enteredDate(UPDATED_ENTERED_DATE)
            .modifiedBy(UPDATED_MODIFIED_BY)
            .modifiedDate(UPDATED_MODIFIED_DATE)
            .eventStatus(UPDATED_EVENT_STATUS);
        EventDTO eventDTO = eventMapper.toDto(updatedEvent);

        restEventMockMvc
            .perform(
                put(ENTITY_API_URL_ID, eventDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(eventDTO))
            )
            .andExpect(status().isOk());

        // Validate the Event in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedEventToMatchAllProperties(updatedEvent);
    }

    @Test
    @Transactional
    void putNonExistingEvent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        event.setId(longCount.incrementAndGet());

        // Create the Event
        EventDTO eventDTO = eventMapper.toDto(event);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEventMockMvc
            .perform(
                put(ENTITY_API_URL_ID, eventDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(eventDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Event in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEvent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        event.setId(longCount.incrementAndGet());

        // Create the Event
        EventDTO eventDTO = eventMapper.toDto(event);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(eventDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Event in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEvent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        event.setId(longCount.incrementAndGet());

        // Create the Event
        EventDTO eventDTO = eventMapper.toDto(event);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(eventDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Event in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEventWithPatch() throws Exception {
        // Initialize the database
        insertedEvent = eventRepository.saveAndFlush(event);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the event using partial update
        Event partialUpdatedEvent = new Event();
        partialUpdatedEvent.setId(event.getId());

        partialUpdatedEvent
            .eventName(UPDATED_EVENT_NAME)
            .eventDate(UPDATED_EVENT_DATE)
            .eventLocation(UPDATED_EVENT_LOCATION)
            .eventPoster(UPDATED_EVENT_POSTER)
            .eventPosterContentType(UPDATED_EVENT_POSTER_CONTENT_TYPE)
            .enteredDate(UPDATED_ENTERED_DATE)
            .modifiedBy(UPDATED_MODIFIED_BY)
            .eventStatus(UPDATED_EVENT_STATUS);

        restEventMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEvent.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEvent))
            )
            .andExpect(status().isOk());

        // Validate the Event in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEventUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedEvent, event), getPersistedEvent(event));
    }

    @Test
    @Transactional
    void fullUpdateEventWithPatch() throws Exception {
        // Initialize the database
        insertedEvent = eventRepository.saveAndFlush(event);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the event using partial update
        Event partialUpdatedEvent = new Event();
        partialUpdatedEvent.setId(event.getId());

        partialUpdatedEvent
            .eventName(UPDATED_EVENT_NAME)
            .eventDesc(UPDATED_EVENT_DESC)
            .eventOrg(UPDATED_EVENT_ORG)
            .eventDate(UPDATED_EVENT_DATE)
            .eventLocation(UPDATED_EVENT_LOCATION)
            .eventPoster(UPDATED_EVENT_POSTER)
            .eventPosterContentType(UPDATED_EVENT_POSTER_CONTENT_TYPE)
            .enteredBy(UPDATED_ENTERED_BY)
            .enteredDate(UPDATED_ENTERED_DATE)
            .modifiedBy(UPDATED_MODIFIED_BY)
            .modifiedDate(UPDATED_MODIFIED_DATE)
            .eventStatus(UPDATED_EVENT_STATUS);

        restEventMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEvent.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEvent))
            )
            .andExpect(status().isOk());

        // Validate the Event in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEventUpdatableFieldsEquals(partialUpdatedEvent, getPersistedEvent(partialUpdatedEvent));
    }

    @Test
    @Transactional
    void patchNonExistingEvent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        event.setId(longCount.incrementAndGet());

        // Create the Event
        EventDTO eventDTO = eventMapper.toDto(event);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEventMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, eventDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(eventDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Event in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEvent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        event.setId(longCount.incrementAndGet());

        // Create the Event
        EventDTO eventDTO = eventMapper.toDto(event);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(eventDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Event in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEvent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        event.setId(longCount.incrementAndGet());

        // Create the Event
        EventDTO eventDTO = eventMapper.toDto(event);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(eventDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Event in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEvent() throws Exception {
        // Initialize the database
        insertedEvent = eventRepository.saveAndFlush(event);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the event
        restEventMockMvc
            .perform(delete(ENTITY_API_URL_ID, event.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return eventRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Event getPersistedEvent(Event event) {
        return eventRepository.findById(event.getId()).orElseThrow();
    }

    protected void assertPersistedEventToMatchAllProperties(Event expectedEvent) {
        assertEventAllPropertiesEquals(expectedEvent, getPersistedEvent(expectedEvent));
    }

    protected void assertPersistedEventToMatchUpdatableProperties(Event expectedEvent) {
        assertEventAllUpdatablePropertiesEquals(expectedEvent, getPersistedEvent(expectedEvent));
    }
}
