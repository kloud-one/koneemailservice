package com.kone.emailservice.web.rest;

import com.kone.emailservice.KoneemailserviceApp;
import com.kone.emailservice.config.SecurityBeanOverrideConfiguration;
import com.kone.emailservice.domain.UserEmailLog;
import com.kone.emailservice.repository.UserEmailLogRepository;
import com.kone.emailservice.service.UserEmailLogService;
import com.kone.emailservice.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static com.kone.emailservice.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@Link UserEmailLogResource} REST controller.
 */
@SpringBootTest(classes = {SecurityBeanOverrideConfiguration.class, KoneemailserviceApp.class})
public class UserEmailLogResourceIT {

    private static final Long DEFAULT_ORG_USER_ID = 1L;
    private static final Long UPDATED_ORG_USER_ID = 2L;

    private static final Boolean DEFAULT_MAIL_SEND_STATUS = false;
    private static final Boolean UPDATED_MAIL_SEND_STATUS = true;

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_SUBJECT = "AAAAAAAAAA";
    private static final String UPDATED_SUBJECT = "BBBBBBBBBB";

    private static final String DEFAULT_MSG_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_MSG_CONTENT = "BBBBBBBBBB";

    private static final Instant DEFAULT_LAST_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private UserEmailLogRepository userEmailLogRepository;

    @Autowired
    private UserEmailLogService userEmailLogService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restUserEmailLogMockMvc;

    private UserEmailLog userEmailLog;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final UserEmailLogResource userEmailLogResource = new UserEmailLogResource(userEmailLogService);
        this.restUserEmailLogMockMvc = MockMvcBuilders.standaloneSetup(userEmailLogResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserEmailLog createEntity(EntityManager em) {
        UserEmailLog userEmailLog = new UserEmailLog()
            .orgUserId(DEFAULT_ORG_USER_ID)
            .mailSendStatus(DEFAULT_MAIL_SEND_STATUS)
            .createdDate(DEFAULT_CREATED_DATE)
            .subject(DEFAULT_SUBJECT)
            .msgContent(DEFAULT_MSG_CONTENT)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE);
        return userEmailLog;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserEmailLog createUpdatedEntity(EntityManager em) {
        UserEmailLog userEmailLog = new UserEmailLog()
            .orgUserId(UPDATED_ORG_USER_ID)
            .mailSendStatus(UPDATED_MAIL_SEND_STATUS)
            .createdDate(UPDATED_CREATED_DATE)
            .subject(UPDATED_SUBJECT)
            .msgContent(UPDATED_MSG_CONTENT)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        return userEmailLog;
    }

    @BeforeEach
    public void initTest() {
        userEmailLog = createEntity(em);
    }

    @Test
    @Transactional
    public void createUserEmailLog() throws Exception {
        int databaseSizeBeforeCreate = userEmailLogRepository.findAll().size();

        // Create the UserEmailLog
        restUserEmailLogMockMvc.perform(post("/api/user-email-logs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userEmailLog)))
            .andExpect(status().isCreated());

        // Validate the UserEmailLog in the database
        List<UserEmailLog> userEmailLogList = userEmailLogRepository.findAll();
        assertThat(userEmailLogList).hasSize(databaseSizeBeforeCreate + 1);
        UserEmailLog testUserEmailLog = userEmailLogList.get(userEmailLogList.size() - 1);
        assertThat(testUserEmailLog.getOrgUserId()).isEqualTo(DEFAULT_ORG_USER_ID);
        assertThat(testUserEmailLog.isMailSendStatus()).isEqualTo(DEFAULT_MAIL_SEND_STATUS);
        assertThat(testUserEmailLog.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testUserEmailLog.getSubject()).isEqualTo(DEFAULT_SUBJECT);
        assertThat(testUserEmailLog.getMsgContent()).isEqualTo(DEFAULT_MSG_CONTENT);
        assertThat(testUserEmailLog.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    public void createUserEmailLogWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = userEmailLogRepository.findAll().size();

        // Create the UserEmailLog with an existing ID
        userEmailLog.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserEmailLogMockMvc.perform(post("/api/user-email-logs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userEmailLog)))
            .andExpect(status().isBadRequest());

        // Validate the UserEmailLog in the database
        List<UserEmailLog> userEmailLogList = userEmailLogRepository.findAll();
        assertThat(userEmailLogList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllUserEmailLogs() throws Exception {
        // Initialize the database
        userEmailLogRepository.saveAndFlush(userEmailLog);

        // Get all the userEmailLogList
        restUserEmailLogMockMvc.perform(get("/api/user-email-logs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userEmailLog.getId().intValue())))
            .andExpect(jsonPath("$.[*].orgUserId").value(hasItem(DEFAULT_ORG_USER_ID.intValue())))
            .andExpect(jsonPath("$.[*].mailSendStatus").value(hasItem(DEFAULT_MAIL_SEND_STATUS.booleanValue())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].subject").value(hasItem(DEFAULT_SUBJECT.toString())))
            .andExpect(jsonPath("$.[*].msgContent").value(hasItem(DEFAULT_MSG_CONTENT.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));
    }
    
    @Test
    @Transactional
    public void getUserEmailLog() throws Exception {
        // Initialize the database
        userEmailLogRepository.saveAndFlush(userEmailLog);

        // Get the userEmailLog
        restUserEmailLogMockMvc.perform(get("/api/user-email-logs/{id}", userEmailLog.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(userEmailLog.getId().intValue()))
            .andExpect(jsonPath("$.orgUserId").value(DEFAULT_ORG_USER_ID.intValue()))
            .andExpect(jsonPath("$.mailSendStatus").value(DEFAULT_MAIL_SEND_STATUS.booleanValue()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.subject").value(DEFAULT_SUBJECT.toString()))
            .andExpect(jsonPath("$.msgContent").value(DEFAULT_MSG_CONTENT.toString()))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingUserEmailLog() throws Exception {
        // Get the userEmailLog
        restUserEmailLogMockMvc.perform(get("/api/user-email-logs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateUserEmailLog() throws Exception {
        // Initialize the database
        userEmailLogService.save(userEmailLog);

        int databaseSizeBeforeUpdate = userEmailLogRepository.findAll().size();

        // Update the userEmailLog
        UserEmailLog updatedUserEmailLog = userEmailLogRepository.findById(userEmailLog.getId()).get();
        // Disconnect from session so that the updates on updatedUserEmailLog are not directly saved in db
        em.detach(updatedUserEmailLog);
        updatedUserEmailLog
            .orgUserId(UPDATED_ORG_USER_ID)
            .mailSendStatus(UPDATED_MAIL_SEND_STATUS)
            .createdDate(UPDATED_CREATED_DATE)
            .subject(UPDATED_SUBJECT)
            .msgContent(UPDATED_MSG_CONTENT)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restUserEmailLogMockMvc.perform(put("/api/user-email-logs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedUserEmailLog)))
            .andExpect(status().isOk());

        // Validate the UserEmailLog in the database
        List<UserEmailLog> userEmailLogList = userEmailLogRepository.findAll();
        assertThat(userEmailLogList).hasSize(databaseSizeBeforeUpdate);
        UserEmailLog testUserEmailLog = userEmailLogList.get(userEmailLogList.size() - 1);
        assertThat(testUserEmailLog.getOrgUserId()).isEqualTo(UPDATED_ORG_USER_ID);
        assertThat(testUserEmailLog.isMailSendStatus()).isEqualTo(UPDATED_MAIL_SEND_STATUS);
        assertThat(testUserEmailLog.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testUserEmailLog.getSubject()).isEqualTo(UPDATED_SUBJECT);
        assertThat(testUserEmailLog.getMsgContent()).isEqualTo(UPDATED_MSG_CONTENT);
        assertThat(testUserEmailLog.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingUserEmailLog() throws Exception {
        int databaseSizeBeforeUpdate = userEmailLogRepository.findAll().size();

        // Create the UserEmailLog

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserEmailLogMockMvc.perform(put("/api/user-email-logs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userEmailLog)))
            .andExpect(status().isBadRequest());

        // Validate the UserEmailLog in the database
        List<UserEmailLog> userEmailLogList = userEmailLogRepository.findAll();
        assertThat(userEmailLogList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteUserEmailLog() throws Exception {
        // Initialize the database
        userEmailLogService.save(userEmailLog);

        int databaseSizeBeforeDelete = userEmailLogRepository.findAll().size();

        // Delete the userEmailLog
        restUserEmailLogMockMvc.perform(delete("/api/user-email-logs/{id}", userEmailLog.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<UserEmailLog> userEmailLogList = userEmailLogRepository.findAll();
        assertThat(userEmailLogList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserEmailLog.class);
        UserEmailLog userEmailLog1 = new UserEmailLog();
        userEmailLog1.setId(1L);
        UserEmailLog userEmailLog2 = new UserEmailLog();
        userEmailLog2.setId(userEmailLog1.getId());
        assertThat(userEmailLog1).isEqualTo(userEmailLog2);
        userEmailLog2.setId(2L);
        assertThat(userEmailLog1).isNotEqualTo(userEmailLog2);
        userEmailLog1.setId(null);
        assertThat(userEmailLog1).isNotEqualTo(userEmailLog2);
    }
}
