package com.jadepeng.simplepv.domain;

import java.io.Serializable;
import javax.persistence.*;
import org.springframework.stereotype.Indexed;

/**
 * A PV.
 */
@Entity
@Table(name = "pv")
public class PV implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "host")
    private String host;

    @Column(name = "url")
    private String url;

    @Column(name = "pv")
    private Integer pv;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PV id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHost() {
        return this.host;
    }

    public PV host(String host) {
        this.setHost(host);
        return this;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getUrl() {
        return this.url;
    }

    public PV url(String url) {
        this.setUrl(url);
        return this;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getPv() {
        return this.pv;
    }

    public PV pv(Integer pv) {
        this.setPv(pv);
        return this;
    }

    public void setPv(Integer pv) {
        this.pv = pv;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PV)) {
            return false;
        }
        return id != null && id.equals(((PV) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PV{" +
            "id=" + getId() +
            ", host='" + getHost() + "'" +
            ", url='" + getUrl() + "'" +
            ", pv=" + getPv() +
            "}";
    }
}
