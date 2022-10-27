/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2022
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.common;

import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class IOUtilsTest {

    @Test
    void getInputStream() throws IOException {
        InputStream is = IOUtils.getInputStream(getClass().getClassLoader(), "FM_10_0_c5_0.testcases");

        BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));

        List<String> lines = new LinkedList<>();
        String line;
        while ((line = br.readLine()) != null) {
            lines.add(line);
        }

        assertAll(() -> assertEquals(6, lines.size()),
                () -> assertEquals("5", lines.get(0)),
                () -> assertEquals("~gui_builder & uml & sdi & ~mdi", lines.get(1)),
                () -> assertEquals("~gui_builder & diagram_builder & ~uml", lines.get(2)),
                () -> assertEquals("~interface & ~gui_builder & diagram_builder & ~uml & sdi", lines.get(3)),
                () -> assertEquals("~interface & ~diagram_builder & uml & mdi", lines.get(4)),
                () -> assertEquals("~mdi & interface", lines.get(5)));
    }
}