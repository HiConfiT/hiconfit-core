/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2022
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.kb.app;

import at.tugraz.ist.ase.kb.app.cli.KBStatistics_CmdLineOptions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Files;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

class KBStatisticsTest {

    String programTitle = "KBStatisticsTest";
    String usage = "Usage: java -jar KBStatisticsTest.jar [options]";

    @Test
    void testPC() {
        String[] args = new String[]{"-kb", "PC"};

        KBStatistics_CmdLineOptions options = new KBStatistics_CmdLineOptions(null, programTitle, null, usage);
        options.parseArgument(args);

        KBStatistics kbStatistics = new KBStatistics(options);

        assertDoesNotThrow(kbStatistics::calculate);

        File file = new File("statistics.txt");
        assertTrue(file.exists());

        // read the content from file
        AtomicReference<String> content = new AtomicReference<>("");
        assertDoesNotThrow(() -> content.set(String.join("\n", Files.readAllLines(file.toPath()))));

        String expected = """
                1
                Name: PCConfigurationProblem
                Source: https://www.itu.dk/research/cla/externals/clib/
                #variables: 45
                #constraints: 36
                #Choco variables: 1158
                #Choco constraints: 644
                Consistency: true""";
        assertEquals(expected, content.get());
    }

    @Test
    void shouldHaveOutput() {
        String[] args = new String[]{"-kb", "PC", "-out", "testPC.txt"};

        KBStatistics_CmdLineOptions options = new KBStatistics_CmdLineOptions(null, programTitle, null, usage);
        options.parseArgument(args);

        KBStatistics kbStatistics = new KBStatistics(options);

        assertDoesNotThrow(kbStatistics::calculate);

        File file = new File("testPC.txt");
        assertTrue(file.exists());

        // read the content from file
        AtomicReference<String> content = new AtomicReference<>("");
        assertDoesNotThrow(() -> content.set(String.join("\n", Files.readAllLines(file.toPath()))));

        String expected = """
                1
                Name: PCConfigurationProblem
                Source: https://www.itu.dk/research/cla/externals/clib/
                #variables: 45
                #constraints: 36
                #Choco variables: 1158
                #Choco constraints: 644
                Consistency: true""";
        assertEquals(expected, content.get());
    }

//    @Test
//    void testRenault() {
//        String[] args = new String[]{"-kb", "Renault"};
//
//        KBStatistics_CmdLineOptions options = new KBStatistics_CmdLineOptions(null, programTitle, null, usage);
//        options.parseArgument(args);
//
//        KBStatistics kbStatistics = new KBStatistics(options);
//
//        assertDoesNotThrow(kbStatistics::calculate);
//
//        File file = new File("statistics.txt");
//        assertTrue(file.exists());
//
//        // read the content from file
//        AtomicReference<String> content = new AtomicReference<>("");
//        assertDoesNotThrow(() -> content.set(String.join("\n", Files.readAllLines(file.toPath()))));
//
//        String expected = """
//                1
//                Name: RenaultConfigurationProblem
//                Source: https://www.itu.dk/research/cla/externals/clib/
//                #variables: 99
//                #constraints: 113
//                #Choco variables: 3178088
//                #Choco constraints: 1694394
//                Consistency: true""";
//        assertEquals(expected, content.get());
//    }

    @Test
    void testOneFM() {
        String[] args = new String[]{"-fm", "./src/test/resources/smartwatch.sxfm"};

        KBStatistics_CmdLineOptions options = new KBStatistics_CmdLineOptions(null, programTitle, null, usage);
        options.parseArgument(args);

        KBStatistics kbStatistics = new KBStatistics(options);

        assertDoesNotThrow(kbStatistics::calculate);

        File file = new File("statistics.txt");
        assertTrue(file.exists());

        // read the content from file
        AtomicReference<String> content = new AtomicReference<>("");
        assertDoesNotThrow(() -> content.set(String.join("\n", Files.readAllLines(file.toPath()))));

        String expected = """
                    1
                    Name: smartwatch.sxfm
                    Source: SPLOT
                    #variables: 12
                    #constraints: 10
                    #Choco variables: 28
                    #Choco constraints: 23
                    Consistency: true
                    
                    CTC ratio: 0.4
                    #features: 12
                    #relationships: 6
                    #constraints: 4
                    #MANDATORY: 2
                    #OPTIONAL: 2
                    #ALTERNATIVE: 1
                    #OR: 1
                    #REQUIRES: 2
                    #EXCLUDES: 1""";
        assertEquals(expected, content.get());
    }

    @Test
    void testManyFMs() {
        String[] args = new String[]{"-fm-dir", "./src/test/resources/fms"};

        KBStatistics_CmdLineOptions options = new KBStatistics_CmdLineOptions(null, programTitle, null, usage);
        options.parseArgument(args);

        KBStatistics kbStatistics = new KBStatistics(options);

        assertDoesNotThrow(kbStatistics::calculate);

        File file = new File("statistics.txt");
        assertTrue(file.exists());

        // read the content from file
        AtomicReference<String> content = new AtomicReference<>("");
        assertDoesNotThrow(() -> content.set(String.join("\n", Files.readAllLines(file.toPath()))));

        String expected = """
                    1
                    Name: bamboobike.xmi
                    Source: SPLOT
                    #variables: 11
                    #constraints: 8
                    #Choco variables: 26
                    #Choco constraints: 20
                    Consistency: true
                    
                    CTC ratio: 0.25
                    #features: 11
                    #relationships: 6
                    #constraints: 2
                    #MANDATORY: 2
                    #OPTIONAL: 2
                    #ALTERNATIVE: 1
                    #OR: 1
                    #REQUIRES: 1
                    #EXCLUDES: 1
                    2
                    Name: bamboobike_featureide.xml
                    Source: SPLOT
                    #variables: 11
                    #constraints: 8
                    #Choco variables: 26
                    #Choco constraints: 20
                    Consistency: true
                    
                    CTC ratio: 0.25
                    #features: 11
                    #relationships: 6
                    #constraints: 2
                    #MANDATORY: 2
                    #OPTIONAL: 2
                    #ALTERNATIVE: 1
                    #OR: 1
                    #REQUIRES: 1
                    #EXCLUDES: 1
                    3
                    Name: bamboobike.gfm.json
                    Source: SPLOT
                    #variables: 11
                    #constraints: 8
                    #Choco variables: 26
                    #Choco constraints: 20
                    Consistency: true
                    
                    CTC ratio: 0.25
                    #features: 11
                    #relationships: 6
                    #constraints: 2
                    #MANDATORY: 2
                    #OPTIONAL: 2
                    #ALTERNATIVE: 1
                    #OR: 1
                    #REQUIRES: 1
                    #EXCLUDES: 1
                    4
                    Name: bamboobike.fm4conf
                    Source: SPLOT
                    #variables: 11
                    #constraints: 8
                    #Choco variables: 26
                    #Choco constraints: 20
                    Consistency: true
                    
                    CTC ratio: 0.25
                    #features: 11
                    #relationships: 6
                    #constraints: 2
                    #MANDATORY: 2
                    #OPTIONAL: 2
                    #ALTERNATIVE: 1
                    #OR: 1
                    #REQUIRES: 1
                    #EXCLUDES: 1
                    5
                    Name: ubuntu.splx
                    Source: SPLOT
                    #variables: 263
                    #constraints: 261
                    #Choco variables: 601
                    #Choco constraints: 608
                    Consistency: true
                    
                    CTC ratio: 0.3218390804597701
                    #features: 263
                    #relationships: 177
                    #constraints: 84
                    #MANDATORY: 67
                    #OPTIONAL: 72
                    #ALTERNATIVE: 37
                    #OR: 1
                    #REQUIRES: 65
                    #EXCLUDES: 0
                    6
                    Name: bamboobike_splot.sxfm
                    Source: SPLOT
                    #variables: 11
                    #constraints: 8
                    #Choco variables: 26
                    #Choco constraints: 20
                    Consistency: true
                    
                    CTC ratio: 0.25
                    #features: 11
                    #relationships: 6
                    #constraints: 2
                    #MANDATORY: 2
                    #OPTIONAL: 2
                    #ALTERNATIVE: 1
                    #OR: 1
                    #REQUIRES: 1
                    #EXCLUDES: 1
                    7
                    Name: smartwatch.sxfm
                    Source: SPLOT
                    #variables: 12
                    #constraints: 10
                    #Choco variables: 28
                    #Choco constraints: 23
                    Consistency: true
                    
                    CTC ratio: 0.4
                    #features: 12
                    #relationships: 6
                    #constraints: 4
                    #MANDATORY: 2
                    #OPTIONAL: 2
                    #ALTERNATIVE: 1
                    #OR: 1
                    #REQUIRES: 2
                    #EXCLUDES: 1""";
        assertEquals(expected, content.get());
    }

    @Test
    void testManyFMsAndKB() {
        String[] args = new String[]{"-fm-dir", "./src/test/resources/fms", "-kb", "PC"}; // , "Renault"

        KBStatistics_CmdLineOptions options = new KBStatistics_CmdLineOptions(null, programTitle, null, usage);
        options.parseArgument(args);

        KBStatistics kbStatistics = new KBStatistics(options);

        assertDoesNotThrow(kbStatistics::calculate);

        File file = new File("statistics.txt");
        assertTrue(file.exists());

        // read the content from file
        AtomicReference<String> content = new AtomicReference<>("");
        assertDoesNotThrow(() -> content.set(String.join("\n", Files.readAllLines(file.toPath()))));

        String expected = """
                        1
                        Name: PCConfigurationProblem
                        Source: https://www.itu.dk/research/cla/externals/clib/
                        #variables: 45
                        #constraints: 36
                        #Choco variables: 1158
                        #Choco constraints: 644
                        Consistency: true
                        2
                        Name: bamboobike.xmi
                        Source: SPLOT
                        #variables: 11
                        #constraints: 8
                        #Choco variables: 26
                        #Choco constraints: 20
                        Consistency: true
                        
                        CTC ratio: 0.25
                        #features: 11
                        #relationships: 6
                        #constraints: 2
                        #MANDATORY: 2
                        #OPTIONAL: 2
                        #ALTERNATIVE: 1
                        #OR: 1
                        #REQUIRES: 1
                        #EXCLUDES: 1
                        3
                        Name: bamboobike_featureide.xml
                        Source: SPLOT
                        #variables: 11
                        #constraints: 8
                        #Choco variables: 26
                        #Choco constraints: 20
                        Consistency: true
                        
                        CTC ratio: 0.25
                        #features: 11
                        #relationships: 6
                        #constraints: 2
                        #MANDATORY: 2
                        #OPTIONAL: 2
                        #ALTERNATIVE: 1
                        #OR: 1
                        #REQUIRES: 1
                        #EXCLUDES: 1
                        4
                        Name: bamboobike.gfm.json
                        Source: SPLOT
                        #variables: 11
                        #constraints: 8
                        #Choco variables: 26
                        #Choco constraints: 20
                        Consistency: true
                        
                        CTC ratio: 0.25
                        #features: 11
                        #relationships: 6
                        #constraints: 2
                        #MANDATORY: 2
                        #OPTIONAL: 2
                        #ALTERNATIVE: 1
                        #OR: 1
                        #REQUIRES: 1
                        #EXCLUDES: 1
                        5
                        Name: bamboobike.fm4conf
                        Source: SPLOT
                        #variables: 11
                        #constraints: 8
                        #Choco variables: 26
                        #Choco constraints: 20
                        Consistency: true
                        
                        CTC ratio: 0.25
                        #features: 11
                        #relationships: 6
                        #constraints: 2
                        #MANDATORY: 2
                        #OPTIONAL: 2
                        #ALTERNATIVE: 1
                        #OR: 1
                        #REQUIRES: 1
                        #EXCLUDES: 1
                        6
                        Name: ubuntu.splx
                        Source: SPLOT
                        #variables: 263
                        #constraints: 261
                        #Choco variables: 601
                        #Choco constraints: 608
                        Consistency: true
                        
                        CTC ratio: 0.3218390804597701
                        #features: 263
                        #relationships: 177
                        #constraints: 84
                        #MANDATORY: 67
                        #OPTIONAL: 72
                        #ALTERNATIVE: 37
                        #OR: 1
                        #REQUIRES: 65
                        #EXCLUDES: 0
                        7
                        Name: bamboobike_splot.sxfm
                        Source: SPLOT
                        #variables: 11
                        #constraints: 8
                        #Choco variables: 26
                        #Choco constraints: 20
                        Consistency: true
                        
                        CTC ratio: 0.25
                        #features: 11
                        #relationships: 6
                        #constraints: 2
                        #MANDATORY: 2
                        #OPTIONAL: 2
                        #ALTERNATIVE: 1
                        #OR: 1
                        #REQUIRES: 1
                        #EXCLUDES: 1
                        8
                        Name: smartwatch.sxfm
                        Source: SPLOT
                        #variables: 12
                        #constraints: 10
                        #Choco variables: 28
                        #Choco constraints: 23
                        Consistency: true
                        
                        CTC ratio: 0.4
                        #features: 12
                        #relationships: 6
                        #constraints: 4
                        #MANDATORY: 2
                        #OPTIONAL: 2
                        #ALTERNATIVE: 1
                        #OR: 1
                        #REQUIRES: 2
                        #EXCLUDES: 1""";
        assertEquals(expected, content.get());
//        2
//        Name: RenaultConfigurationProblem
//        Source: https://www.itu.dk/research/cla/externals/clib/
//                        #variables: 99
//                        #constraints: 113
//                        #Choco variables: 3178088
//                        #Choco constraints: 1694394
//        Consistency: true
    }
}