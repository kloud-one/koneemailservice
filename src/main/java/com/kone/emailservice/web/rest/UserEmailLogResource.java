package com.kone.emailservice.web.rest;

import com.kone.emailservice.domain.EmailDTO;
import com.kone.emailservice.domain.UserEmailLog;
import com.kone.emailservice.service.OrgUserEmailInfoService;
import com.kone.emailservice.service.UserEmailLogService;
import com.kone.emailservice.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.kone.emailservice.domain.UserEmailLog}.
 */
@RestController
@RequestMapping("/api")
public class UserEmailLogResource {

    private final Logger log = LoggerFactory.getLogger(UserEmailLogResource.class);

    private static final String ENTITY_NAME = "koneemailserviceUserEmailLog";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserEmailLogService userEmailLogService;
    private final OrgUserEmailInfoService orgUserEmailInfoService;

    public UserEmailLogResource(UserEmailLogService userEmailLogService, OrgUserEmailInfoService orgUserEmailInfoService) {
        this.userEmailLogService = userEmailLogService;
        this.orgUserEmailInfoService = orgUserEmailInfoService;
    }

    /**
     * {@code POST  /user-email-logs} : Create a new userEmailLog.
     *
     * @param userEmailLog the userEmailLog to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new userEmailLog, or with status {@code 400 (Bad Request)} if the userEmailLog has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/user-email-logs")
    public ResponseEntity<UserEmailLog> createUserEmailLog(@RequestBody UserEmailLog userEmailLog) throws URISyntaxException {
        log.debug("REST request to save UserEmailLog : {}", userEmailLog);
        if (userEmailLog.getId() != null) {
            throw new BadRequestAlertException("A new userEmailLog cannot already have an ID", ENTITY_NAME, "idexists");
        }
        UserEmailLog result = userEmailLogService.save(userEmailLog);
        return ResponseEntity.created(new URI("/api/user-email-logs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /user-email-logs} : Updates an existing userEmailLog.
     *
     * @param userEmailLog the userEmailLog to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userEmailLog,
     * or with status {@code 400 (Bad Request)} if the userEmailLog is not valid,
     * or with status {@code 500 (Internal Server Error)} if the userEmailLog couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/user-email-logs")
    public ResponseEntity<UserEmailLog> updateUserEmailLog(@RequestBody UserEmailLog userEmailLog) throws URISyntaxException {
        log.debug("REST request to update UserEmailLog : {}", userEmailLog);
        if (userEmailLog.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        UserEmailLog result = userEmailLogService.save(userEmailLog);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, userEmailLog.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /user-email-logs} : get all the userEmailLogs.
     *
     * @param pageable the pagination information.
     * @param queryParams a {@link MultiValueMap} query parameters.
     * @param uriBuilder a {@link UriComponentsBuilder} URI builder.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of userEmailLogs in body.
     */
    @GetMapping("/user-email-logs")
    public ResponseEntity<List<UserEmailLog>> getAllUserEmailLogs(Pageable pageable, @RequestParam MultiValueMap<String, String> queryParams, UriComponentsBuilder uriBuilder) {
        log.debug("REST request to get a page of UserEmailLogs");
        Page<UserEmailLog> page = userEmailLogService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(uriBuilder.queryParams(queryParams), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /user-email-logs/:id} : get the "id" userEmailLog.
     *
     * @param id the id of the userEmailLog to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the userEmailLog, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/user-email-logs/{id}")
    public ResponseEntity<UserEmailLog> getUserEmailLog(@PathVariable Long id) {
        log.debug("REST request to get UserEmailLog : {}", id);
        Optional<UserEmailLog> userEmailLog = userEmailLogService.findOne(id);
        return ResponseUtil.wrapOrNotFound(userEmailLog);
    }

    /**
     * {@code DELETE  /user-email-logs/:id} : delete the "id" userEmailLog.
     *
     * @param id the id of the userEmailLog to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/user-email-logs/{id}")
    public ResponseEntity<Void> deleteUserEmailLog(@PathVariable Long id) {
        log.debug("REST request to delete UserEmailLog : {}", id);
        userEmailLogService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }

    @PostMapping("/sendEmail")
    public ResponseEntity<UserEmailLog> sendEmailToUser(@RequestBody EmailDTO emailDTO) {
        log.debug("REST request to send Email To User : {}", emailDTO.getToEmail());
        Optional<UserEmailLog> result = userEmailLogService.onSendEmailToUser(emailDTO);
        return ResponseEntity.accepted().body(result.orElseThrow(RuntimeException::new));
    }
}
