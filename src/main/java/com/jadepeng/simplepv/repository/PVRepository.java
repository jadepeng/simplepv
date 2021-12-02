package com.jadepeng.simplepv.repository;

import com.jadepeng.simplepv.domain.PV;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the PV entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PVRepository extends JpaRepository<PV, Long> {
    Optional<PV> findFirstByUrl(String url);
}
