/*
 * High Performance Knowledge Based Configuration Techniques
 *
 * Copyright (c) 2022-2023
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.common;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import lombok.NonNull;
import lombok.experimental.UtilityClass;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import static com.google.common.base.Preconditions.checkArgument;

@UtilityClass
public class IOUtils {
    /**
     * Gets InputStream of a file in the resource
     * @param classLoader the classLoader of the program
     * @param file filename
     * @return an InputStream to the required file
     * @throws FileNotFoundException throws an exception when couldn't find the required file in the resource of the program
     */
    public InputStream getInputStream(@NonNull ClassLoader classLoader, @NonNull String file) throws IOException {
        InputStream inputStream = classLoader.getResourceAsStream(file);

        if (inputStream == null) {
            throw new FileNotFoundException(file + " doesn't exist!");
        }
        return inputStream;
    }

    public CSVReader getCSVReader(@NonNull InputStream inputStream) {
        CSVParser csvParser = new CSVParserBuilder().withSeparator(';').build();
        return new CSVReaderBuilder(new InputStreamReader(inputStream, StandardCharsets.UTF_8))
                .withCSVParser(csvParser).build();
    }

    public boolean checkAndCreateFolder(@NonNull String path) {
        checkArgument(!path.isEmpty(), "Path is empty!");

        File folder = new File(path);

        // check whether the folder does not exist
        if (Files.notExists(Paths.get(path))) {
            return folder.mkdir(); // if not, create it
        }
        return false;
    }
}
