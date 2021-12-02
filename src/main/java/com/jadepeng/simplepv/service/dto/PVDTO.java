package com.jadepeng.simplepv.service.dto;

import com.jadepeng.simplepv.domain.User;

/**
 * A DTO representing a user, with only the public attributes.
 */
public class PVDTO {

    long site_pv;

    long page_pv;

    public PVDTO(long page_pv, long site_pv) {
        this.site_pv = site_pv;
        this.page_pv = page_pv;
    }

    public long getSite_pv() {
        return site_pv;
    }

    public void setSite_pv(long site_pv) {
        this.site_pv = site_pv;
    }

    public long getPage_pv() {
        return page_pv;
    }

    public void setPage_pv(long page_pv) {
        this.page_pv = page_pv;
    }
}
