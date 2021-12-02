package com.jadepeng.simplepv.web.rest;

import com.alibaba.fastjson.support.spring.annotation.ResponseJSONP;
import com.jadepeng.simplepv.domain.PV;
import com.jadepeng.simplepv.repository.PVRepository;
import com.jadepeng.simplepv.service.PVService;
import com.jadepeng.simplepv.service.dto.PVDTO;
import com.jadepeng.simplepv.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.servlet.http.HttpServletResponse;
import org.bouncycastle.util.encoders.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.jadepeng.simplepv.domain.PV}.
 */
@RestController
@RequestMapping("/api")
public class PVResource {

    private final Logger log = LoggerFactory.getLogger(PVResource.class);

    private static final String ENTITY_NAME = "pV";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PVService pVService;

    private final PVRepository pVRepository;

    public PVResource(PVService pVService, PVRepository pVRepository) {
        this.pVService = pVService;
        this.pVRepository = pVRepository;
    }

    @GetMapping("/pv/{data}")
    @ResponseJSONP
    public PVDTO pv(@PathVariable(value = "data") final String data) {
        String url = new String(Base64.decode(data));
        return this.pVService.increment(url);
    }

    /**
     * {@code POST  /pvs} : Create a new pV.
     *
     * @param pV the pV to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new pV, or with status {@code 400 (Bad Request)} if the pV has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/pvs")
    public ResponseEntity<PV> createPV(@RequestBody PV pV) throws URISyntaxException {
        log.debug("REST request to save PV : {}", pV);
        if (pV.getId() != null) {
            throw new BadRequestAlertException("A new pV cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PV result = pVService.save(pV);
        return ResponseEntity
            .created(new URI("/api/pvs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /pvs/:id} : Updates an existing pV.
     *
     * @param id the id of the pV to save.
     * @param pV the pV to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pV,
     * or with status {@code 400 (Bad Request)} if the pV is not valid,
     * or with status {@code 500 (Internal Server Error)} if the pV couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/pvs/{id}")
    public ResponseEntity<PV> updatePV(@PathVariable(value = "id", required = false) final Long id, @RequestBody PV pV)
        throws URISyntaxException {
        log.debug("REST request to update PV : {}, {}", id, pV);
        if (pV.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pV.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pVRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PV result = pVService.save(pV);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, pV.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /pvs/:id} : Partial updates given fields of an existing pV, field will ignore if it is null
     *
     * @param id the id of the pV to save.
     * @param pV the pV to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pV,
     * or with status {@code 400 (Bad Request)} if the pV is not valid,
     * or with status {@code 404 (Not Found)} if the pV is not found,
     * or with status {@code 500 (Internal Server Error)} if the pV couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/pvs/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PV> partialUpdatePV(@PathVariable(value = "id", required = false) final Long id, @RequestBody PV pV)
        throws URISyntaxException {
        log.debug("REST request to partial update PV partially : {}, {}", id, pV);
        if (pV.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pV.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pVRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PV> result = pVService.partialUpdate(pV);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, pV.getId().toString())
        );
    }

    /**
     * {@code GET  /pvs} : get all the pVS.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of pVS in body.
     */
    @GetMapping("/pvs")
    public List<PV> getAllPVS() {
        log.debug("REST request to get all PVS");
        return pVService.findAll();
    }

    /**
     * {@code GET  /pvs/:id} : get the "id" pV.
     *
     * @param id the id of the pV to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the pV, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/pvs/{id}")
    public ResponseEntity<PV> getPV(@PathVariable Long id) {
        log.debug("REST request to get PV : {}", id);
        Optional<PV> pV = pVService.findOne(id);
        return ResponseUtil.wrapOrNotFound(pV);
    }

    /**
     * {@code DELETE  /pvs/:id} : delete the "id" pV.
     *
     * @param id the id of the pV to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/pvs/{id}")
    public ResponseEntity<Void> deletePV(@PathVariable Long id) {
        log.debug("REST request to delete PV : {}", id);
        pVService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
