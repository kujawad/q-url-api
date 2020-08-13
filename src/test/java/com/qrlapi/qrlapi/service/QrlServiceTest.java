package com.qrlapi.qrlapi.service;

import com.qrlapi.qrlapi.model.Qrl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static com.qrlapi.qrlapi.util.QrlTestUtils.qrls;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@ActiveProfiles("test")
@SpringBootTest
public class QrlServiceTest {

    @Autowired
    private QrlService qrlService;

    @BeforeEach
    public void before() {
        qrlService.purge();
    }

    @Test
    public void shouldFindAllQrls() {
        // given
        final int expectedSize = 3;
        final List<Qrl> qrls = qrls();

        // when
        qrlService.addQrl(qrls.get(0));
        qrlService.addQrl(qrls.get(1));
        qrlService.addQrl(qrls.get(2));

        // then
        final int actualSize = qrlService.getAllQrls().size();
        assertEquals(expectedSize, actualSize);
    }

    @Test
    public void shouldAddQrl() {
        // given
        final Qrl expected = Qrl.builder().stamp("stamp").url("url").build();

        // when
        qrlService.addQrl(expected);

        // then
        final Qrl actual = qrlService.findQrlById(expected.getId());
        assertEquals(expected, actual);
    }

    @Test
    public void shouldFindQrlByStamp() {
        // given
        final String stamp = "stamp";
        final Qrl expected = Qrl.builder().stamp(stamp).url("url").build();

        // when
        qrlService.addQrl(expected);

        // then
        final Qrl actual = qrlService.findQrlByStamp(stamp);
        assertEquals(expected, actual);
    }

    @Test
    public void shouldFindQrlById() {
        // given
        final Qrl expected = Qrl.builder().stamp("stamp").url("url").build();

        // when
        qrlService.addQrl(expected);

        // then
        final Qrl actual = qrlService.findQrlById(expected.getId());
        assertEquals(expected, actual);
    }

    @Test
    public void shouldRemoveQrl() {
        // given
        final Qrl qrl = Qrl.builder().stamp("stamp").url("url").build();
        qrlService.addQrl(qrl);

        // when
        qrlService.removeQrl(qrl);

        // then
        final Qrl actual = qrlService.findQrlById(qrl.getId());
        assertNull(actual);
    }

    @Test
    public void shouldPurge() {
        // given
        final int expectedSize = 0;
        final List<Qrl> qrls = qrls();

        qrlService.addQrl(qrls.get(0));
        qrlService.addQrl(qrls.get(1));
        qrlService.addQrl(qrls.get(2));

        // when
        qrlService.purge();

        // then
        final int actualSize = qrlService.getAllQrls().size();
        assertEquals(expectedSize, actualSize);
    }

    @Test
    public void shouldCreateJson() {
        // given
        final Qrl qrl = Qrl.builder().stamp("stamp").url("url").build();
        final String expected = "{\"url\":\"url\",\"stamp\":\"stamp\"}";

        // when
        final String actual = qrlService.createJson(qrl);

        // then
        assertEquals(expected, actual);
    }
}
