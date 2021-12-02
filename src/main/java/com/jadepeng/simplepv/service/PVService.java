package com.jadepeng.simplepv.service;

import com.jadepeng.simplepv.domain.PV;
import com.jadepeng.simplepv.service.dto.PVDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link PV}.
 */
public interface PVService {
    PVDTO increment(String url);

    /**
     * Save a pV.
     *
     * @param pV the entity to save.
     * @return the persisted entity.
     */
    PV save(PV pV);

    /**
     * Partially updates a pV.
     *
     * @param pV the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PV> partialUpdate(PV pV);

    /**
     * Get all the pVS.
     *
     * @return the list of entities.
     */
    List<PV> findAll();

    /**
     * Get the "id" pV.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PV> findOne(Long id);

    /**
     * Delete the "id" pV.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
