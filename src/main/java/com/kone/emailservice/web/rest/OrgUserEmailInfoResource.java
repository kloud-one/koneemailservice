package com.kone.emailservice.web.rest;

import com.kone.emailservice.domain.OrgUserEmailInfo;
import com.kone.emailservice.service.OrgUserEmailInfoService;
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
import org.springframework.http.HttpStatus;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.kone.emailservice.domain.OrgUserEmailInfo}.
 */
@RestController
@RequestMapping("/api")
public class OrgUserEmailInfoResource {

    private final Logger log = LoggerFactory.getLogger(OrgUserEmailInfoResource.class);

    private static final String ENTITY_NAME = "koneemailserviceOrgUserEmailInfo";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OrgUserEmailInfoService orgUserEmailInfoService;

    public OrgUserEmailInfoResource(OrgUserEmailInfoService orgUserEmailInfoService) {
        this.orgUserEmailInfoService = orgUserEmailInfoService;
    }

    /**
     * {@code POST  /org-user-email-infos} : Create a new orgUserEmailInfo.
     *
     * @param orgUserEmailInfo the orgUserEmailInfo to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new orgUserEmailInfo, or with status {@code 400 (Bad Request)} if the orgUserEmailInfo has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/org-user-email-infos")
    public ResponseEntity<OrgUserEmailInfo> createOrgUserEmailInfo(@RequestBody OrgUserEmailInfo orgUserEmailInfo) throws URISyntaxException {
        log.debug("REST request to save OrgUserEmailInfo : {}", orgUserEmailInfo);
        if (orgUserEmailInfo.getId() != null) {
            throw new BadRequestAlertException("A new orgUserEmailInfo cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OrgUserEmailInfo result = orgUserEmailInfoService.save(orgUserEmailInfo);
        return ResponseEntity.created(new URI("/api/org-user-email-infos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /org-user-email-infos} : Updates an existing orgUserEmailInfo.
     *
     * @param orgUserEmailInfo the orgUserEmailInfo to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated orgUserEmailInfo,
     * or with status {@code 400 (Bad Request)} if the orgUserEmailInfo is not valid,
     * or with status {@code 500 (Internal Server Error)} if the orgUserEmailInfo couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/org-user-email-infos")
    public ResponseEntity<OrgUserEmailInfo> updateOrgUserEmailInfo(@RequestBody OrgUserEmailInfo orgUserEmailInfo) throws URISyntaxException {
        log.debug("REST request to update OrgUserEmailInfo : {}", orgUserEmailInfo);
        if (orgUserEmailInfo.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        OrgUserEmailInfo result = orgUserEmailInfoService.save(orgUserEmailInfo);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, orgUserEmailInfo.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /org-user-email-infos} : get all the orgUserEmailInfos.
     *
     * @param pageable the pagination information.
     * @param queryParams a {@link MultiValueMap} query parameters.
     * @param uriBuilder a {@link UriComponentsBuilder} URI builder.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of orgUserEmailInfos in body.
     */
    @GetMapping("/org-user-email-infos")
    public ResponseEntity<List<OrgUserEmailInfo>> getAllOrgUserEmailInfos(Pageable pageable, @RequestParam MultiValueMap<String, String> queryParams, UriComponentsBuilder uriBuilder) {
        log.debug("REST request to get a page of OrgUserEmailInfos");
        Page<OrgUserEmailInfo> page = orgUserEmailInfoService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(uriBuilder.queryParams(queryParams), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /org-user-email-infos/:id} : get the "id" orgUserEmailInfo.
     *
     * @param id the id of the orgUserEmailInfo to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the orgUserEmailInfo, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/org-user-email-infos/{id}")
    public ResponseEntity<OrgUserEmailInfo> getOrgUserEmailInfo(@PathVariable Long id) {
        log.debug("REST request to get OrgUserEmailInfo : {}", id);
        Optional<OrgUserEmailInfo> orgUserEmailInfo = orgUserEmailInfoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(orgUserEmailInfo);
    }

    /**
     * {@code DELETE  /org-user-email-infos/:id} : delete the "id" orgUserEmailInfo.
     *
     * @param id the id of the orgUserEmailInfo to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/org-user-email-infos/{id}")
    public ResponseEntity<Void> deleteOrgUserEmailInfo(@PathVariable Long id) {
        log.debug("REST request to delete OrgUserEmailInfo : {}", id);
        orgUserEmailInfoService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
