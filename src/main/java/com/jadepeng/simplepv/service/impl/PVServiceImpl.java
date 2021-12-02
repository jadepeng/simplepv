package com.jadepeng.simplepv.service.impl;

import com.jadepeng.simplepv.domain.PV;
import com.jadepeng.simplepv.repository.PVRepository;
import com.jadepeng.simplepv.service.PVService;
import com.jadepeng.simplepv.service.dto.PVDTO;
import com.jadepeng.simplepv.service.lock.Lock;
import com.jadepeng.simplepv.service.lock.LockExistsException;
import com.jadepeng.simplepv.service.lock.LockService;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link PV}.
 */
@Service
@Transactional
public class PVServiceImpl implements PVService {

    private final Logger log = LoggerFactory.getLogger(PVServiceImpl.class);

    private final PVRepository pVRepository;

    private final LockService lockService;

    public PVServiceImpl(PVRepository pVRepository, LockService lockService) {
        this.pVRepository = pVRepository;
        this.lockService = lockService;
    }

    @Override
    public PVDTO increment(String url) {
        Lock lock = null;
        // 简单锁一下 TODO待优化
        try {
            URL uri = new URL(url);
            lock = this.lock(uri.getHost(), 30000);
            if (lock == null) {
                throw new RuntimeException("领取文件失败，请稍后重试");
            }

            PV pv = incrementPV(url);

            PV sitePv = incrementPV(uri.getHost());

            return new PVDTO(pv.getPv(), sitePv.getPv());
        } catch (MalformedURLException e) {
            throw new RuntimeException("url not support");
        } finally {
            if (lock != null) {
                this.releaseLock(lock);
            }
        }
    }

    @Override
    public PV save(PV pV) {
        log.debug("Request to save PV : {}", pV);
        return pVRepository.save(pV);
    }

    @Override
    public Optional<PV> partialUpdate(PV pV) {
        log.debug("Request to partially update PV : {}", pV);

        return pVRepository
            .findById(pV.getId())
            .map(existingPV -> {
                if (pV.getHost() != null) {
                    existingPV.setHost(pV.getHost());
                }
                if (pV.getUrl() != null) {
                    existingPV.setUrl(pV.getUrl());
                }
                if (pV.getPv() != null) {
                    existingPV.setPv(pV.getPv());
                }

                return existingPV;
            })
            .map(pVRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PV> findAll() {
        log.debug("Request to get all PVS");
        return pVRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PV> findOne(Long id) {
        log.debug("Request to get PV : {}", id);
        return pVRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete PV : {}", id);
        pVRepository.deleteById(id);
    }

    private PV incrementPV(String url) {
        PV pv = this.pVRepository.findFirstByUrl(url).orElse(new PV().url(url).pv(0));
        pv.setPv(pv.getPv() + 1);
        this.pVRepository.save(pv);
        return pv;
    }

    @SneakyThrows
    private Lock lock(String name, int timeout) {
        Lock lock = null;
        long startTime = System.currentTimeMillis();
        while (true) {
            try {
                lock = lockService.create(name);
                return lock;
            } catch (LockExistsException e) {
                if (System.currentTimeMillis() - startTime >= timeout) {
                    return null;
                }
                Thread.sleep(100);
            }
        }
    }

    private void releaseLock(Lock lock) {
        lockService.release(lock.getName(), lock.getValue());
    }
}
