package com.kone.emailservice.service;

import com.kone.emailservice.domain.OrgUserEmailInfo;
import com.kone.emailservice.repository.OrgUserEmailInfoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link OrgUserEmailInfo}.
 */
@Service
@Transactional
public class OrgUserEmailInfoService {

    private final Logger log = LoggerFactory.getLogger(OrgUserEmailInfoService.class);

    private final OrgUserEmailInfoRepository orgUserEmailInfoRepository;

    public OrgUserEmailInfoService(OrgUserEmailInfoRepository orgUserEmailInfoRepository) {
        this.orgUserEmailInfoRepository = orgUserEmailInfoRepository;
    }

    /**
     * Save a orgUserEmailInfo.
     *
     * @param orgUserEmailInfo the entity to save.
     * @return the persisted entity.
     */
    public OrgUserEmailInfo save(OrgUserEmailInfo orgUserEmailInfo) {
        log.debug("Request to save OrgUserEmailInfo : {}", orgUserEmailInfo);
        return orgUserEmailInfoRepository.save(orgUserEmailInfo);
    }

    /**
     * Get all the orgUserEmailInfos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<OrgUserEmailInfo> findAll(Pageable pageable) {
        log.debug("Request to get all OrgUserEmailInfos");
        return orgUserEmailInfoRepository.findAll(pageable);
    }


    /**
     * Get one orgUserEmailInfo by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<OrgUserEmailInfo> findOne(Long id) {
        log.debug("Request to get OrgUserEmailInfo : {}", id);
        return orgUserEmailInfoRepository.findById(id);
    }

    /**
     * Delete the orgUserEmailInfo by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete OrgUserEmailInfo : {}", id);
        orgUserEmailInfoRepository.deleteById(id);
    }

    public Optional<OrgUserEmailInfo> onGetOrgUserId(String organizationName, String orgMemberEmailId) {
        log.debug("Request to fetch OrgUserId using Organization Name and Organization Member Email Id : {}", orgMemberEmailId);
        return orgUserEmailInfoRepository.findOneByOrganizationNameAndOrgMemberEmailId(organizationName, orgMemberEmailId);
    }

    /**
     * Save a distinct orgUserEmailInfo.
     *
     * @param organizationName the entity to save.
     * @return the persisted entity.
     */
    public Integer saveDistinct(String organizationName, String orgMemberEmailId) {
        log.debug("Request to save OrgUserEmailInfo : {}", organizationName);
        return orgUserEmailInfoRepository.saveDistinct(organizationName, orgMemberEmailId);
    }
}
