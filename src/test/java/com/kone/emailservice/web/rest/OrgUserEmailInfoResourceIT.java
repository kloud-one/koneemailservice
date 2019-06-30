package com.kone.emailservice.web.rest;

import com.kone.emailservice.KoneemailserviceApp;
import com.kone.emailservice.config.SecurityBeanOverrideConfiguration;
import com.kone.emailservice.domain.OrgUserEmailInfo;
import com.kone.emailservice.repository.OrgUserEmailInfoRepository;
import com.kone.emailservice.service.OrgUserEmailInfoService;
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
 * Integration tests for the {@Link OrgUserEmailInfoResource} REST controller.
 */
@SpringBootTest(classes = {SecurityBeanOverrideConfiguration.class, KoneemailserviceApp.class})
public class OrgUserEmailInfoResourceIT {

    private static final String DEFAULT_ORGANIZATION_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ORGANIZATION_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ORG_MEMBER_EMAIL_ID = "AAAAAAAAAA";
    private static final String UPDATED_ORG_MEMBER_EMAIL_ID = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_LAST_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private OrgUserEmailInfoRepository orgUserEmailInfoRepository;

    @Autowired
    private OrgUserEmailInfoService orgUserEmailInfoService;

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

    private MockMvc restOrgUserEmailInfoMockMvc;

    private OrgUserEmailInfo orgUserEmailInfo;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final OrgUserEmailInfoResource orgUserEmailInfoResource = new OrgUserEmailInfoResource(orgUserEmailInfoService);
        this.restOrgUserEmailInfoMockMvc = MockMvcBuilders.standaloneSetup(orgUserEmailInfoResource)
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
    public static OrgUserEmailInfo createEntity(EntityManager em) {
        OrgUserEmailInfo orgUserEmailInfo = new OrgUserEmailInfo()
            .organizationName(DEFAULT_ORGANIZATION_NAME)
            .orgMemberEmailId(DEFAULT_ORG_MEMBER_EMAIL_ID)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE);
        return orgUserEmailInfo;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrgUserEmailInfo createUpdatedEntity(EntityManager em) {
        OrgUserEmailInfo orgUserEmailInfo = new OrgUserEmailInfo()
            .organizationName(UPDATED_ORGANIZATION_NAME)
            .orgMemberEmailId(UPDATED_ORG_MEMBER_EMAIL_ID)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        return orgUserEmailInfo;
    }

    @BeforeEach
    public void initTest() {
        orgUserEmailInfo = createEntity(em);
    }

    @Test
    @Transactional
    public void createOrgUserEmailInfo() throws Exception {
        int databaseSizeBeforeCreate = orgUserEmailInfoRepository.findAll().size();

        // Create the OrgUserEmailInfo
        restOrgUserEmailInfoMockMvc.perform(post("/api/org-user-email-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(orgUserEmailInfo)))
            .andExpect(status().isCreated());

        // Validate the OrgUserEmailInfo in the database
        List<OrgUserEmailInfo> orgUserEmailInfoList = orgUserEmailInfoRepository.findAll();
        assertThat(orgUserEmailInfoList).hasSize(databaseSizeBeforeCreate + 1);
        OrgUserEmailInfo testOrgUserEmailInfo = orgUserEmailInfoList.get(orgUserEmailInfoList.size() - 1);
        assertThat(testOrgUserEmailInfo.getOrganizationName()).isEqualTo(DEFAULT_ORGANIZATION_NAME);
        assertThat(testOrgUserEmailInfo.getOrgMemberEmailId()).isEqualTo(DEFAULT_ORG_MEMBER_EMAIL_ID);
        assertThat(testOrgUserEmailInfo.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testOrgUserEmailInfo.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    public void createOrgUserEmailInfoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = orgUserEmailInfoRepository.findAll().size();

        // Create the OrgUserEmailInfo with an existing ID
        orgUserEmailInfo.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrgUserEmailInfoMockMvc.perform(post("/api/org-user-email-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(orgUserEmailInfo)))
            .andExpect(status().isBadRequest());

        // Validate the OrgUserEmailInfo in the database
        List<OrgUserEmailInfo> orgUserEmailInfoList = orgUserEmailInfoRepository.findAll();
        assertThat(orgUserEmailInfoList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllOrgUserEmailInfos() throws Exception {
        // Initialize the database
        orgUserEmailInfoRepository.saveAndFlush(orgUserEmailInfo);

        // Get all the orgUserEmailInfoList
        restOrgUserEmailInfoMockMvc.perform(get("/api/org-user-email-infos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(orgUserEmailInfo.getId().intValue())))
            .andExpect(jsonPath("$.[*].organizationName").value(hasItem(DEFAULT_ORGANIZATION_NAME.toString())))
            .andExpect(jsonPath("$.[*].orgMemberEmailId").value(hasItem(DEFAULT_ORG_MEMBER_EMAIL_ID.toString())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));
    }
    
    @Test
    @Transactional
    public void getOrgUserEmailInfo() throws Exception {
        // Initialize the database
        orgUserEmailInfoRepository.saveAndFlush(orgUserEmailInfo);

        // Get the orgUserEmailInfo
        restOrgUserEmailInfoMockMvc.perform(get("/api/org-user-email-infos/{id}", orgUserEmailInfo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(orgUserEmailInfo.getId().intValue()))
            .andExpect(jsonPath("$.organizationName").value(DEFAULT_ORGANIZATION_NAME.toString()))
            .andExpect(jsonPath("$.orgMemberEmailId").value(DEFAULT_ORG_MEMBER_EMAIL_ID.toString()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingOrgUserEmailInfo() throws Exception {
        // Get the orgUserEmailInfo
        restOrgUserEmailInfoMockMvc.perform(get("/api/org-user-email-infos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateOrgUserEmailInfo() throws Exception {
        // Initialize the database
        orgUserEmailInfoService.save(orgUserEmailInfo);

        int databaseSizeBeforeUpdate = orgUserEmailInfoRepository.findAll().size();

        // Update the orgUserEmailInfo
        OrgUserEmailInfo updatedOrgUserEmailInfo = orgUserEmailInfoRepository.findById(orgUserEmailInfo.getId()).get();
        // Disconnect from session so that the updates on updatedOrgUserEmailInfo are not directly saved in db
        em.detach(updatedOrgUserEmailInfo);
        updatedOrgUserEmailInfo
            .organizationName(UPDATED_ORGANIZATION_NAME)
            .orgMemberEmailId(UPDATED_ORG_MEMBER_EMAIL_ID)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restOrgUserEmailInfoMockMvc.perform(put("/api/org-user-email-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedOrgUserEmailInfo)))
            .andExpect(status().isOk());

        // Validate the OrgUserEmailInfo in the database
        List<OrgUserEmailInfo> orgUserEmailInfoList = orgUserEmailInfoRepository.findAll();
        assertThat(orgUserEmailInfoList).hasSize(databaseSizeBeforeUpdate);
        OrgUserEmailInfo testOrgUserEmailInfo = orgUserEmailInfoList.get(orgUserEmailInfoList.size() - 1);
        assertThat(testOrgUserEmailInfo.getOrganizationName()).isEqualTo(UPDATED_ORGANIZATION_NAME);
        assertThat(testOrgUserEmailInfo.getOrgMemberEmailId()).isEqualTo(UPDATED_ORG_MEMBER_EMAIL_ID);
        assertThat(testOrgUserEmailInfo.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testOrgUserEmailInfo.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingOrgUserEmailInfo() throws Exception {
        int databaseSizeBeforeUpdate = orgUserEmailInfoRepository.findAll().size();

        // Create the OrgUserEmailInfo

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrgUserEmailInfoMockMvc.perform(put("/api/org-user-email-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(orgUserEmailInfo)))
            .andExpect(status().isBadRequest());

        // Validate the OrgUserEmailInfo in the database
        List<OrgUserEmailInfo> orgUserEmailInfoList = orgUserEmailInfoRepository.findAll();
        assertThat(orgUserEmailInfoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteOrgUserEmailInfo() throws Exception {
        // Initialize the database
        orgUserEmailInfoService.save(orgUserEmailInfo);

        int databaseSizeBeforeDelete = orgUserEmailInfoRepository.findAll().size();

        // Delete the orgUserEmailInfo
        restOrgUserEmailInfoMockMvc.perform(delete("/api/org-user-email-infos/{id}", orgUserEmailInfo.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<OrgUserEmailInfo> orgUserEmailInfoList = orgUserEmailInfoRepository.findAll();
        assertThat(orgUserEmailInfoList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrgUserEmailInfo.class);
        OrgUserEmailInfo orgUserEmailInfo1 = new OrgUserEmailInfo();
        orgUserEmailInfo1.setId(1L);
        OrgUserEmailInfo orgUserEmailInfo2 = new OrgUserEmailInfo();
        orgUserEmailInfo2.setId(orgUserEmailInfo1.getId());
        assertThat(orgUserEmailInfo1).isEqualTo(orgUserEmailInfo2);
        orgUserEmailInfo2.setId(2L);
        assertThat(orgUserEmailInfo1).isNotEqualTo(orgUserEmailInfo2);
        orgUserEmailInfo1.setId(null);
        assertThat(orgUserEmailInfo1).isNotEqualTo(orgUserEmailInfo2);
    }
}
