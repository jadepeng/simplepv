package com.jadepeng.simplepv.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.jadepeng.simplepv.IntegrationTest;
import com.jadepeng.simplepv.domain.PV;
import com.jadepeng.simplepv.repository.PVRepository;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link PVResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PVResourceIT {

    private static final String DEFAULT_HOST = "AAAAAAAAAA";
    private static final String UPDATED_HOST = "BBBBBBBBBB";

    private static final String DEFAULT_URL = "AAAAAAAAAA";
    private static final String UPDATED_URL = "BBBBBBBBBB";

    private static final Integer DEFAULT_PV = 1;
    private static final Integer UPDATED_PV = 2;

    private static final String ENTITY_API_URL = "/api/pvs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PVRepository pVRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPVMockMvc;

    private PV pV;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PV createEntity(EntityManager em) {
        PV pV = new PV().host(DEFAULT_HOST).url(DEFAULT_URL).pv(DEFAULT_PV);
        return pV;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PV createUpdatedEntity(EntityManager em) {
        PV pV = new PV().host(UPDATED_HOST).url(UPDATED_URL).pv(UPDATED_PV);
        return pV;
    }

    @BeforeEach
    public void initTest() {
        pV = createEntity(em);
    }

    @Test
    @Transactional
    void createPV() throws Exception {
        int databaseSizeBeforeCreate = pVRepository.findAll().size();
        // Create the PV
        restPVMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pV)))
            .andExpect(status().isCreated());

        // Validate the PV in the database
        List<PV> pVList = pVRepository.findAll();
        assertThat(pVList).hasSize(databaseSizeBeforeCreate + 1);
        PV testPV = pVList.get(pVList.size() - 1);
        assertThat(testPV.getHost()).isEqualTo(DEFAULT_HOST);
        assertThat(testPV.getUrl()).isEqualTo(DEFAULT_URL);
        assertThat(testPV.getPv()).isEqualTo(DEFAULT_PV);
    }

    @Test
    @Transactional
    void createPVWithExistingId() throws Exception {
        // Create the PV with an existing ID
        pV.setId(1L);

        int databaseSizeBeforeCreate = pVRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPVMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pV)))
            .andExpect(status().isBadRequest());

        // Validate the PV in the database
        List<PV> pVList = pVRepository.findAll();
        assertThat(pVList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPVS() throws Exception {
        // Initialize the database
        pVRepository.saveAndFlush(pV);

        // Get all the pVList
        restPVMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pV.getId().intValue())))
            .andExpect(jsonPath("$.[*].host").value(hasItem(DEFAULT_HOST)))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL)))
            .andExpect(jsonPath("$.[*].pv").value(hasItem(DEFAULT_PV)));
    }

    @Test
    @Transactional
    void getPV() throws Exception {
        // Initialize the database
        pVRepository.saveAndFlush(pV);

        // Get the pV
        restPVMockMvc
            .perform(get(ENTITY_API_URL_ID, pV.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(pV.getId().intValue()))
            .andExpect(jsonPath("$.host").value(DEFAULT_HOST))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL))
            .andExpect(jsonPath("$.pv").value(DEFAULT_PV));
    }

    @Test
    @Transactional
    void getNonExistingPV() throws Exception {
        // Get the pV
        restPVMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPV() throws Exception {
        // Initialize the database
        pVRepository.saveAndFlush(pV);

        int databaseSizeBeforeUpdate = pVRepository.findAll().size();

        // Update the pV
        PV updatedPV = pVRepository.findById(pV.getId()).get();
        // Disconnect from session so that the updates on updatedPV are not directly saved in db
        em.detach(updatedPV);
        updatedPV.host(UPDATED_HOST).url(UPDATED_URL).pv(UPDATED_PV);

        restPVMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPV.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPV))
            )
            .andExpect(status().isOk());

        // Validate the PV in the database
        List<PV> pVList = pVRepository.findAll();
        assertThat(pVList).hasSize(databaseSizeBeforeUpdate);
        PV testPV = pVList.get(pVList.size() - 1);
        assertThat(testPV.getHost()).isEqualTo(UPDATED_HOST);
        assertThat(testPV.getUrl()).isEqualTo(UPDATED_URL);
        assertThat(testPV.getPv()).isEqualTo(UPDATED_PV);
    }

    @Test
    @Transactional
    void putNonExistingPV() throws Exception {
        int databaseSizeBeforeUpdate = pVRepository.findAll().size();
        pV.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPVMockMvc
            .perform(
                put(ENTITY_API_URL_ID, pV.getId()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pV))
            )
            .andExpect(status().isBadRequest());

        // Validate the PV in the database
        List<PV> pVList = pVRepository.findAll();
        assertThat(pVList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPV() throws Exception {
        int databaseSizeBeforeUpdate = pVRepository.findAll().size();
        pV.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPVMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pV))
            )
            .andExpect(status().isBadRequest());

        // Validate the PV in the database
        List<PV> pVList = pVRepository.findAll();
        assertThat(pVList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPV() throws Exception {
        int databaseSizeBeforeUpdate = pVRepository.findAll().size();
        pV.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPVMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pV)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PV in the database
        List<PV> pVList = pVRepository.findAll();
        assertThat(pVList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePVWithPatch() throws Exception {
        // Initialize the database
        pVRepository.saveAndFlush(pV);

        int databaseSizeBeforeUpdate = pVRepository.findAll().size();

        // Update the pV using partial update
        PV partialUpdatedPV = new PV();
        partialUpdatedPV.setId(pV.getId());

        partialUpdatedPV.host(UPDATED_HOST).pv(UPDATED_PV);

        restPVMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPV.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPV))
            )
            .andExpect(status().isOk());

        // Validate the PV in the database
        List<PV> pVList = pVRepository.findAll();
        assertThat(pVList).hasSize(databaseSizeBeforeUpdate);
        PV testPV = pVList.get(pVList.size() - 1);
        assertThat(testPV.getHost()).isEqualTo(UPDATED_HOST);
        assertThat(testPV.getUrl()).isEqualTo(DEFAULT_URL);
        assertThat(testPV.getPv()).isEqualTo(UPDATED_PV);
    }

    @Test
    @Transactional
    void fullUpdatePVWithPatch() throws Exception {
        // Initialize the database
        pVRepository.saveAndFlush(pV);

        int databaseSizeBeforeUpdate = pVRepository.findAll().size();

        // Update the pV using partial update
        PV partialUpdatedPV = new PV();
        partialUpdatedPV.setId(pV.getId());

        partialUpdatedPV.host(UPDATED_HOST).url(UPDATED_URL).pv(UPDATED_PV);

        restPVMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPV.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPV))
            )
            .andExpect(status().isOk());

        // Validate the PV in the database
        List<PV> pVList = pVRepository.findAll();
        assertThat(pVList).hasSize(databaseSizeBeforeUpdate);
        PV testPV = pVList.get(pVList.size() - 1);
        assertThat(testPV.getHost()).isEqualTo(UPDATED_HOST);
        assertThat(testPV.getUrl()).isEqualTo(UPDATED_URL);
        assertThat(testPV.getPv()).isEqualTo(UPDATED_PV);
    }

    @Test
    @Transactional
    void patchNonExistingPV() throws Exception {
        int databaseSizeBeforeUpdate = pVRepository.findAll().size();
        pV.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPVMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, pV.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pV))
            )
            .andExpect(status().isBadRequest());

        // Validate the PV in the database
        List<PV> pVList = pVRepository.findAll();
        assertThat(pVList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPV() throws Exception {
        int databaseSizeBeforeUpdate = pVRepository.findAll().size();
        pV.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPVMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pV))
            )
            .andExpect(status().isBadRequest());

        // Validate the PV in the database
        List<PV> pVList = pVRepository.findAll();
        assertThat(pVList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPV() throws Exception {
        int databaseSizeBeforeUpdate = pVRepository.findAll().size();
        pV.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPVMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(pV)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PV in the database
        List<PV> pVList = pVRepository.findAll();
        assertThat(pVList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePV() throws Exception {
        // Initialize the database
        pVRepository.saveAndFlush(pV);

        int databaseSizeBeforeDelete = pVRepository.findAll().size();

        // Delete the pV
        restPVMockMvc.perform(delete(ENTITY_API_URL_ID, pV.getId()).accept(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PV> pVList = pVRepository.findAll();
        assertThat(pVList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
