package com.kone.emailservice.service;

import com.kone.emailservice.domain.EmailDTO;
import com.kone.emailservice.domain.OrgUserEmailInfo;
import com.kone.emailservice.domain.SubscriptionInfo;
import com.kone.emailservice.domain.UserEmailLog;
import com.kone.emailservice.repository.UserEmailLogRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;

/**
 * Service Implementation for managing {@link UserEmailLog}.
 */
@Service
@Transactional
public class UserEmailLogService {

    private final Logger log = LoggerFactory.getLogger(UserEmailLogService.class);

    private final UserEmailLogRepository userEmailLogRepository;
    private final MailerService mailerService;
    private final OrgUserEmailInfoService orgUserEmailInfoService;
    private final SubscriptionInfoService subscriptionInfoService;

    public UserEmailLogService(UserEmailLogRepository userEmailLogRepository, MailerService mailerService,
                               OrgUserEmailInfoService orgUserEmailInfoService, SubscriptionInfoService subscriptionInfoService) {
        this.userEmailLogRepository = userEmailLogRepository;
        this.mailerService = mailerService;
        this.orgUserEmailInfoService = orgUserEmailInfoService;
        this.subscriptionInfoService = subscriptionInfoService;
    }

    /**
     * Save a userEmailLog.
     *
     * @param userEmailLog the entity to save.
     * @return the persisted entity.
     */
    public UserEmailLog save(UserEmailLog userEmailLog) {
        log.debug("Request to save UserEmailLog : {}", userEmailLog);
        return userEmailLogRepository.save(userEmailLog);
    }

    /**
     * Get all the userEmailLogs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<UserEmailLog> findAll(Pageable pageable) {
        log.debug("Request to get all UserEmailLogs");
        return userEmailLogRepository.findAll(pageable);
    }


    /**
     * Get one userEmailLog by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<UserEmailLog> findOne(Long id) {
        log.debug("Request to get UserEmailLog : {}", id);
        return userEmailLogRepository.findById(id);
    }

    /**
     * Delete the userEmailLog by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete UserEmailLog : {}", id);
        userEmailLogRepository.deleteById(id);
    }

    /**
     * Send Email To a User.
     *
     * @param emailDTO the entity object for sending email.
     */
    public Optional<UserEmailLog> onSendEmailToUser(EmailDTO emailDTO) {
        log.debug("Request to send email to a user : {}", emailDTO.getToEmail());
        Optional<OrgUserEmailInfo> orgUserEmailInfo = orgUserEmailInfoService.onGetOrgUserId(emailDTO.getOrganizationName(),
            emailDTO.getToEmail());
        if(!orgUserEmailInfo.isPresent()) {
            OrgUserEmailInfo newOrgUserEmailInfo = new OrgUserEmailInfo();
            newOrgUserEmailInfo.setOrganizationName(emailDTO.getOrganizationName());
            newOrgUserEmailInfo.setOrgMemberEmailId(emailDTO.getToEmail());
            orgUserEmailInfoService.save(newOrgUserEmailInfo);
            Optional<OrgUserEmailInfo> resultOrgUserEmailInfo = orgUserEmailInfoService.onGetOrgUserId(emailDTO.getOrganizationName(), emailDTO.getToEmail());
            SubscriptionInfo subscriptionInfo = new SubscriptionInfo();
            subscriptionInfo.setOrguseremailinfo(resultOrgUserEmailInfo.get());
            subscriptionInfoService.save(subscriptionInfo);
            return onSetEmailContent(resultOrgUserEmailInfo.orElseThrow(RuntimeException::new), emailDTO);
        } else {
            Optional<SubscriptionInfo> subscriptionInfo = subscriptionInfoService.onGetSubscriptionInfoByOrgUserId(orgUserEmailInfo.get());
            if(subscriptionInfo.get().isSubscriptionStatus()) {
                return onSetEmailContent(orgUserEmailInfo.get(), emailDTO);
            } else {
                return Optional.empty();
            }
        }
    }

    private Optional<UserEmailLog> onSetEmailContent(OrgUserEmailInfo orgUserEmailInfo, EmailDTO emailDTO) {
        UserEmailLog userEmailLog = new UserEmailLog();
        userEmailLog.setSubject(emailDTO.getSubject());
        userEmailLog.setMsgContent(emailDTO.getMsgContent());
        mailerService.sendEmail(emailDTO);
        userEmailLog.setMailSendStatus(true);
        userEmailLog.setOrguseremailinfo(orgUserEmailInfo);
        userEmailLogRepository.save(userEmailLog);
        return Optional.of(userEmailLog);
    }
}
