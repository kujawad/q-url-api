package com.qrlapi.qrlapi.dao;

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
public class QrlRepositoryTest {

    @Autowired
    private QrlRepository qrlRepository;

    @BeforeEach
    public void before() {
        qrlRepository.deleteAll();
    }

    @Test
    public void shouldSaveQrl() {
        // given
        final Qrl expected = Qrl.builder().stamp("stamp").url("url").build();

        // when
        qrlRepository.save(expected);

        // then
        final Qrl actual = qrlRepository.findById(expected.getId()).orElse(null);
        assertEquals(expected, actual);
    }

    @Test
    public void shouldSaveAll() {
        // given
        final int expectedSize = 3;
        final List<Qrl> qrls = qrls();

        // when
        qrlRepository.saveAll(qrls);

        // then
        final int actualSize = qrlRepository.findAll().size();
        assertEquals(expectedSize, actualSize);
    }

    @Test
    public void shouldFindQrlByStamp() {
        // given
        final String stamp = "stamp";
        final Qrl expected = Qrl.builder().stamp(stamp).url("url").build();

        // when
        qrlRepository.save(expected);

        // then
        final Qrl actual = qrlRepository.findByStamp(stamp);
        assertEquals(expected, actual);
    }

    @Test
    public void shouldDeleteOneQrl() {
        // given
        final Qrl qrl = Qrl.builder().stamp("stamp").url("url").build();
        qrlRepository.save(qrl);

        // when
        qrlRepository.delete(qrl);

        // then
        final Qrl actual = qrlRepository.findById(qrl.getId()).orElse(null);
        assertNull(actual);
    }

    @Test
    public void shouldDeleteAll() {
        // given
        final int expectedSize = 0;
        final List<Qrl> qrls = qrls();

        qrlRepository.saveAll(qrls);

        // when
        qrlRepository.deleteAll();

        // then
        final int actualSize = qrlRepository.findAll().size();
        assertEquals(expectedSize, actualSize);
    }
}
