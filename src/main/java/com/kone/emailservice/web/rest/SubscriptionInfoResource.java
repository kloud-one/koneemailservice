package com.kone.emailservice.web.rest;

import com.kone.emailservice.domain.SubscriptionDTO;
import com.kone.emailservice.domain.SubscriptionInfo;
import com.kone.emailservice.domain.UnsubscriptionDTO;
import com.kone.emailservice.service.OrgUserEmailInfoService;
import com.kone.emailservice.service.SubscriptionInfoService;
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
 * REST controller for managing {@link com.kone.emailservice.domain.SubscriptionInfo}.
 */
@RestController
@RequestMapping("/api")
public class SubscriptionInfoResource {

    private final Logger log = LoggerFactory.getLogger(SubscriptionInfoResource.class);

    private static final String ENTITY_NAME = "koneemailserviceSubscriptionInfo";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SubscriptionInfoService subscriptionInfoService;
    private final OrgUserEmailInfoService orgUserEmailInfoService;

    public SubscriptionInfoResource(SubscriptionInfoService subscriptionInfoService, OrgUserEmailInfoService orgUserEmailInfoService) {
        this.subscriptionInfoService = subscriptionInfoService;
        this.orgUserEmailInfoService = orgUserEmailInfoService;
    }

    /**
     * {@code POST  /subscription-infos} : Create a new subscriptionInfo.
     *
     * @param subscriptionInfo the subscriptionInfo to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new subscriptionInfo, or with status {@code 400 (Bad Request)} if the subscriptionInfo has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/subscription-infos")
    public ResponseEntity<SubscriptionInfo> createSubscriptionInfo(@RequestBody SubscriptionInfo subscriptionInfo) throws URISyntaxException {
        log.debug("REST request to save SubscriptionInfo : {}", subscriptionInfo);
        if (subscriptionInfo.getId() != null) {
            throw new BadRequestAlertException("A new subscriptionInfo cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SubscriptionInfo result = subscriptionInfoService.save(subscriptionInfo);
        return ResponseEntity.created(new URI("/api/subscription-infos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /subscription-infos} : Updates an existing subscriptionInfo.
     *
     * @param subscriptionInfo the subscriptionInfo to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated subscriptionInfo,
     * or with status {@code 400 (Bad Request)} if the subscriptionInfo is not valid,
     * or with status {@code 500 (Internal Server Error)} if the subscriptionInfo couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/subscription-infos")
    public ResponseEntity<SubscriptionInfo> updateSubscriptionInfo(@RequestBody SubscriptionInfo subscriptionInfo) throws URISyntaxException {
        log.debug("REST request to update SubscriptionInfo : {}", subscriptionInfo);
        if (subscriptionInfo.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SubscriptionInfo result = subscriptionInfoService.save(subscriptionInfo);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, subscriptionInfo.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /subscription-infos} : get all the subscriptionInfos.
     *
     * @param pageable the pagination information.
     * @param queryParams a {@link MultiValueMap} query parameters.
     * @param uriBuilder a {@link UriComponentsBuilder} URI builder.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of subscriptionInfos in body.
     */
    @GetMapping("/subscription-infos")
    public ResponseEntity<List<SubscriptionInfo>> getAllSubscriptionInfos(Pageable pageable, @RequestParam MultiValueMap<String, String> queryParams, UriComponentsBuilder uriBuilder) {
        log.debug("REST request to get a page of SubscriptionInfos");
        Page<SubscriptionInfo> page = subscriptionInfoService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(uriBuilder.queryParams(queryParams), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /subscription-infos/:id} : get the "id" subscriptionInfo.
     *
     * @param id the id of the subscriptionInfo to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the subscriptionInfo, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/subscription-infos/{id}")
    public ResponseEntity<SubscriptionInfo> getSubscriptionInfo(@PathVariable Long id) {
        log.debug("REST request to get SubscriptionInfo : {}", id);
        Optional<SubscriptionInfo> subscriptionInfo = subscriptionInfoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(subscriptionInfo);
    }

    /**
     * {@code DELETE  /subscription-infos/:id} : delete the "id" subscriptionInfo.
     *
     * @param id the id of the subscriptionInfo to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/subscription-infos/{id}")
    public ResponseEntity<Void> deleteSubscriptionInfo(@PathVariable Long id) {
        log.debug("REST request to delete SubscriptionInfo : {}", id);
        subscriptionInfoService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }

    @PostMapping("/subscribeUser")
    public ResponseEntity<Void> subscribeUser(@RequestBody List<SubscriptionDTO> subscriptionDTOList) {
        subscriptionInfoService.onSubscribeUser(subscriptionDTOList);
        return ResponseEntity.noContent().headers(HeaderUtil.createAlert(applicationName, "Users are subscribed successfully...", "")).build();
    }

    @PostMapping("/unsubscribeUser")
    public ResponseEntity<Void> unsubscribeUser(@RequestBody List<UnsubscriptionDTO> unsubscriptionDTOList) {
        subscriptionInfoService.onUnSubscribeUser(unsubscriptionDTOList);
        return ResponseEntity.noContent().headers(HeaderUtil.createAlert(applicationName, "Users are unsubscribed successfully...", "")).build();
    }
}
