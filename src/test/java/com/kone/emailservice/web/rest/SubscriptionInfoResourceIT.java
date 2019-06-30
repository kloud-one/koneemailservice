package com.kone.emailservice.web.rest;

import com.kone.emailservice.KoneemailserviceApp;
import com.kone.emailservice.config.SecurityBeanOverrideConfiguration;
import com.kone.emailservice.domain.SubscriptionInfo;
import com.kone.emailservice.repository.SubscriptionInfoRepository;
import com.kone.emailservice.service.SubscriptionInfoService;
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
import org.springframework.util.Base64Utils;
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
 * Integration tests for the {@Link SubscriptionInfoResource} REST controller.
 */
@SpringBootTest(classes = {SecurityBeanOverrideConfiguration.class, KoneemailserviceApp.class})
public class SubscriptionInfoResourceIT {

    private static final Long DEFAULT_ORG_USER_ID = 1L;
    private static final Long UPDATED_ORG_USER_ID = 2L;

    private static final Boolean DEFAULT_SUBSCRIPTION_STATUS = false;
    private static final Boolean UPDATED_SUBSCRIPTION_STATUS = true;

    private static final byte[] DEFAULT_TOKEN = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_TOKEN = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_TOKEN_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_TOKEN_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_REASON = "AAAAAAAAAA";
    private static final String UPDATED_REASON = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_LAST_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private SubscriptionInfoRepository subscriptionInfoRepository;

    @Autowired
    private SubscriptionInfoService subscriptionInfoService;

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

    private MockMvc restSubscriptionInfoMockMvc;

    private SubscriptionInfo subscriptionInfo;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SubscriptionInfoResource subscriptionInfoResource = new SubscriptionInfoResource(subscriptionInfoService);
        this.restSubscriptionInfoMockMvc = MockMvcBuilders.standaloneSetup(subscriptionInfoResource)
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
    public static SubscriptionInfo createEntity(EntityManager em) {
        SubscriptionInfo subscriptionInfo = new SubscriptionInfo()
            .orgUserId(DEFAULT_ORG_USER_ID)
            .subscriptionStatus(DEFAULT_SUBSCRIPTION_STATUS)
            .token(DEFAULT_TOKEN)
            .tokenContentType(DEFAULT_TOKEN_CONTENT_TYPE)
            .reason(DEFAULT_REASON)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE);
        return subscriptionInfo;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SubscriptionInfo createUpdatedEntity(EntityManager em) {
        SubscriptionInfo subscriptionInfo = new SubscriptionInfo()
            .orgUserId(UPDATED_ORG_USER_ID)
            .subscriptionStatus(UPDATED_SUBSCRIPTION_STATUS)
            .token(UPDATED_TOKEN)
            .tokenContentType(UPDATED_TOKEN_CONTENT_TYPE)
            .reason(UPDATED_REASON)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        return subscriptionInfo;
    }

    @BeforeEach
    public void initTest() {
        subscriptionInfo = createEntity(em);
    }

    @Test
    @Transactional
    public void createSubscriptionInfo() throws Exception {
        int databaseSizeBeforeCreate = subscriptionInfoRepository.findAll().size();

        // Create the SubscriptionInfo
        restSubscriptionInfoMockMvc.perform(post("/api/subscription-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(subscriptionInfo)))
            .andExpect(status().isCreated());

        // Validate the SubscriptionInfo in the database
        List<SubscriptionInfo> subscriptionInfoList = subscriptionInfoRepository.findAll();
        assertThat(subscriptionInfoList).hasSize(databaseSizeBeforeCreate + 1);
        SubscriptionInfo testSubscriptionInfo = subscriptionInfoList.get(subscriptionInfoList.size() - 1);
        assertThat(testSubscriptionInfo.getOrgUserId()).isEqualTo(DEFAULT_ORG_USER_ID);
        assertThat(testSubscriptionInfo.isSubscriptionStatus()).isEqualTo(DEFAULT_SUBSCRIPTION_STATUS);
        assertThat(testSubscriptionInfo.getToken()).isEqualTo(DEFAULT_TOKEN);
        assertThat(testSubscriptionInfo.getTokenContentType()).isEqualTo(DEFAULT_TOKEN_CONTENT_TYPE);
        assertThat(testSubscriptionInfo.getReason()).isEqualTo(DEFAULT_REASON);
        assertThat(testSubscriptionInfo.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testSubscriptionInfo.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    public void createSubscriptionInfoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = subscriptionInfoRepository.findAll().size();

        // Create the SubscriptionInfo with an existing ID
        subscriptionInfo.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSubscriptionInfoMockMvc.perform(post("/api/subscription-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(subscriptionInfo)))
            .andExpect(status().isBadRequest());

        // Validate the SubscriptionInfo in the database
        List<SubscriptionInfo> subscriptionInfoList = subscriptionInfoRepository.findAll();
        assertThat(subscriptionInfoList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllSubscriptionInfos() throws Exception {
        // Initialize the database
        subscriptionInfoRepository.saveAndFlush(subscriptionInfo);

        // Get all the subscriptionInfoList
        restSubscriptionInfoMockMvc.perform(get("/api/subscription-infos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(subscriptionInfo.getId().intValue())))
            .andExpect(jsonPath("$.[*].orgUserId").value(hasItem(DEFAULT_ORG_USER_ID.intValue())))
            .andExpect(jsonPath("$.[*].subscriptionStatus").value(hasItem(DEFAULT_SUBSCRIPTION_STATUS.booleanValue())))
            .andExpect(jsonPath("$.[*].tokenContentType").value(hasItem(DEFAULT_TOKEN_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].token").value(hasItem(Base64Utils.encodeToString(DEFAULT_TOKEN))))
            .andExpect(jsonPath("$.[*].reason").value(hasItem(DEFAULT_REASON.toString())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));
    }
    
    @Test
    @Transactional
    public void getSubscriptionInfo() throws Exception {
        // Initialize the database
        subscriptionInfoRepository.saveAndFlush(subscriptionInfo);

        // Get the subscriptionInfo
        restSubscriptionInfoMockMvc.perform(get("/api/subscription-infos/{id}", subscriptionInfo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(subscriptionInfo.getId().intValue()))
            .andExpect(jsonPath("$.orgUserId").value(DEFAULT_ORG_USER_ID.intValue()))
            .andExpect(jsonPath("$.subscriptionStatus").value(DEFAULT_SUBSCRIPTION_STATUS.booleanValue()))
            .andExpect(jsonPath("$.tokenContentType").value(DEFAULT_TOKEN_CONTENT_TYPE))
            .andExpect(jsonPath("$.token").value(Base64Utils.encodeToString(DEFAULT_TOKEN)))
            .andExpect(jsonPath("$.reason").value(DEFAULT_REASON.toString()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingSubscriptionInfo() throws Exception {
        // Get the subscriptionInfo
        restSubscriptionInfoMockMvc.perform(get("/api/subscription-infos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSubscriptionInfo() throws Exception {
        // Initialize the database
        subscriptionInfoService.save(subscriptionInfo);

        int databaseSizeBeforeUpdate = subscriptionInfoRepository.findAll().size();

        // Update the subscriptionInfo
        SubscriptionInfo updatedSubscriptionInfo = subscriptionInfoRepository.findById(subscriptionInfo.getId()).get();
        // Disconnect from session so that the updates on updatedSubscriptionInfo are not directly saved in db
        em.detach(updatedSubscriptionInfo);
        updatedSubscriptionInfo
            .orgUserId(UPDATED_ORG_USER_ID)
            .subscriptionStatus(UPDATED_SUBSCRIPTION_STATUS)
            .token(UPDATED_TOKEN)
            .tokenContentType(UPDATED_TOKEN_CONTENT_TYPE)
            .reason(UPDATED_REASON)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restSubscriptionInfoMockMvc.perform(put("/api/subscription-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedSubscriptionInfo)))
            .andExpect(status().isOk());

        // Validate the SubscriptionInfo in the database
        List<SubscriptionInfo> subscriptionInfoList = subscriptionInfoRepository.findAll();
        assertThat(subscriptionInfoList).hasSize(databaseSizeBeforeUpdate);
        SubscriptionInfo testSubscriptionInfo = subscriptionInfoList.get(subscriptionInfoList.size() - 1);
        assertThat(testSubscriptionInfo.getOrgUserId()).isEqualTo(UPDATED_ORG_USER_ID);
        assertThat(testSubscriptionInfo.isSubscriptionStatus()).isEqualTo(UPDATED_SUBSCRIPTION_STATUS);
        assertThat(testSubscriptionInfo.getToken()).isEqualTo(UPDATED_TOKEN);
        assertThat(testSubscriptionInfo.getTokenContentType()).isEqualTo(UPDATED_TOKEN_CONTENT_TYPE);
        assertThat(testSubscriptionInfo.getReason()).isEqualTo(UPDATED_REASON);
        assertThat(testSubscriptionInfo.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testSubscriptionInfo.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingSubscriptionInfo() throws Exception {
        int databaseSizeBeforeUpdate = subscriptionInfoRepository.findAll().size();

        // Create the SubscriptionInfo

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSubscriptionInfoMockMvc.perform(put("/api/subscription-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(subscriptionInfo)))
            .andExpect(status().isBadRequest());

        // Validate the SubscriptionInfo in the database
        List<SubscriptionInfo> subscriptionInfoList = subscriptionInfoRepository.findAll();
        assertThat(subscriptionInfoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteSubscriptionInfo() throws Exception {
        // Initialize the database
        subscriptionInfoService.save(subscriptionInfo);

        int databaseSizeBeforeDelete = subscriptionInfoRepository.findAll().size();

        // Delete the subscriptionInfo
        restSubscriptionInfoMockMvc.perform(delete("/api/subscription-infos/{id}", subscriptionInfo.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SubscriptionInfo> subscriptionInfoList = subscriptionInfoRepository.findAll();
        assertThat(subscriptionInfoList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SubscriptionInfo.class);
        SubscriptionInfo subscriptionInfo1 = new SubscriptionInfo();
        subscriptionInfo1.setId(1L);
        SubscriptionInfo subscriptionInfo2 = new SubscriptionInfo();
        subscriptionInfo2.setId(subscriptionInfo1.getId());
        assertThat(subscriptionInfo1).isEqualTo(subscriptionInfo2);
        subscriptionInfo2.setId(2L);
        assertThat(subscriptionInfo1).isNotEqualTo(subscriptionInfo2);
        subscriptionInfo1.setId(null);
        assertThat(subscriptionInfo1).isNotEqualTo(subscriptionInfo2);
    }
}
