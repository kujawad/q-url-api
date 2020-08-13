package com.qrlapi.qrlapi.util;

import com.qrlapi.qrlapi.model.Qrl;

import java.util.Arrays;
import java.util.List;

public class QrlTestUtils {

    public static final String URL = "/api/urls";

    public static List<Qrl> qrls() {
        final Qrl qrl1 = Qrl.builder().stamp("stamp1").url("url1").build();
        final Qrl qrl2 = Qrl.builder().stamp("stamp2").url("url2").build();
        final Qrl qrl3 = Qrl.builder().stamp("stamp3").url("url3").build();

        return Arrays.asList(qrl1, qrl2, qrl3);
    }

    public static String expectedGetAllQrls() {
        return "[{\"url\":\"url1\",\"stamp\":\"stamp1\"},{\"url\":\"url2\",\"stamp\":\"stamp2\"},{\"url\":\"url3\",\"stamp\":\"stamp3\"}]";
    }
}
