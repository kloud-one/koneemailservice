package com.kone.emailservice.service;

import com.kone.emailservice.domain.OrgUserEmailInfo;
import com.kone.emailservice.domain.SubscriptionDTO;
import com.kone.emailservice.domain.SubscriptionInfo;
import com.kone.emailservice.domain.UnsubscriptionDTO;
import com.kone.emailservice.repository.SubscriptionInfoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link SubscriptionInfo}.
 */
@Service
@Transactional
public class SubscriptionInfoService {

    private final Logger log = LoggerFactory.getLogger(SubscriptionInfoService.class);

    private final SubscriptionInfoRepository subscriptionInfoRepository;
    private final OrgUserEmailInfoService orgUserEmailInfoService;

    public SubscriptionInfoService(SubscriptionInfoRepository subscriptionInfoRepository, OrgUserEmailInfoService orgUserEmailInfoService) {
        this.subscriptionInfoRepository = subscriptionInfoRepository;
        this.orgUserEmailInfoService = orgUserEmailInfoService;
    }

    /**
     * Save a subscriptionInfo.
     *
     * @param subscriptionInfo the entity to save.
     * @return the persisted entity.
     */
    public SubscriptionInfo save(SubscriptionInfo subscriptionInfo) {
        log.debug("Request to save SubscriptionInfo : {}", subscriptionInfo);
        return subscriptionInfoRepository.save(subscriptionInfo);
    }

    /**
     * Get all the subscriptionInfos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<SubscriptionInfo> findAll(Pageable pageable) {
        log.debug("Request to get all SubscriptionInfos");
        return subscriptionInfoRepository.findAll(pageable);
    }


    /**
     * Get one subscriptionInfo by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SubscriptionInfo> findOne(Long id) {
        log.debug("Request to get SubscriptionInfo : {}", id);
        return subscriptionInfoRepository.findById(id);
    }

    /**
     * Delete the subscriptionInfo by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete SubscriptionInfo : {}", id);
        subscriptionInfoRepository.deleteById(id);
    }

    public Optional<SubscriptionInfo> onGetSubscriptionInfoByOrgUserId(OrgUserEmailInfo orgUserEmailInfo) {
        log.debug("Request to fetch OrgUserId using Organization Name and Organization Member Email Id : {}", orgUserEmailInfo);
        return subscriptionInfoRepository.findOneByorguseremailinfoId(orgUserEmailInfo.getId());
    }

    public void onSubscribeUser(List<SubscriptionDTO> subscriptionDTOList) {
        for(SubscriptionDTO subscriptionDTO : subscriptionDTOList) {
            Optional<SubscriptionInfo> subscriptionInfo = onGetSubscriptionInfoByOrgUserId(subscriptionDTO.getOrgUserEmailInfo());
            if(!subscriptionInfo.get().isSubscriptionStatus()) {
                subscriptionInfo.get().setOrguseremailinfo(subscriptionInfo.get().getOrguseremailinfo());
                subscriptionInfo.get().setSubscriptionStatus(true);
                subscriptionInfo.get().setLastModifiedDate(LocalDate.now());
                subscriptionInfoRepository.save(subscriptionInfo.get());
            } else {
                throw new IllegalArgumentException("This user is already subscribed..." + subscriptionDTO.getOrgUserEmailInfo().getOrgMemberEmailId());
            }
        }
    }


    public void onUnSubscribeUser(List<UnsubscriptionDTO> unsubscriptionDTOList) {
        for(UnsubscriptionDTO unsubscriptionDTO: unsubscriptionDTOList) {
            log.debug("REST request to unsubscribe user from receiving further emails: {}", unsubscriptionDTO.getOrgUserEmailInfo().getOrgMemberEmailId());
            Optional<SubscriptionInfo> subscriptionInfo = onGetSubscriptionInfoByOrgUserId(unsubscriptionDTO.getOrgUserEmailInfo());
            if(subscriptionInfo.get().isSubscriptionStatus()) {
                subscriptionInfo.get().setLastModifiedDate(LocalDate.now());
                subscriptionInfo.get().setSubscriptionStatus(false);
                subscriptionInfo.get().setOrguseremailinfo(unsubscriptionDTO.getOrgUserEmailInfo());
                subscriptionInfo.get().setReason(unsubscriptionDTO.getReason());
                subscriptionInfo.get().setToken(unsubscriptionDTO.getToken());
                subscriptionInfoRepository.save(subscriptionInfo.get());
            } else {
                throw new IllegalArgumentException("This user is already unsubscribed..." + unsubscriptionDTO.getOrgUserEmailInfo().getOrgMemberEmailId());
            }
        }
    }
}
