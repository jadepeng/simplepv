package com.jadepeng.simplepv.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.jadepeng.simplepv.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PVTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PV.class);
        PV pV1 = new PV();
        pV1.setId(1L);
        PV pV2 = new PV();
        pV2.setId(pV1.getId());
        assertThat(pV1).isEqualTo(pV2);
        pV2.setId(2L);
        assertThat(pV1).isNotEqualTo(pV2);
        pV1.setId(null);
        assertThat(pV1).isNotEqualTo(pV2);
    }
}
