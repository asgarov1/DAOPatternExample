package com.asgarov.student.reader;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.stream.Collectors;

public class FileReader {
    public List<String> readFile(String pathToFile) throws IOException {
        return Files.lines(Paths.get(pathToFile)).collect(Collectors.toList());
    }
}
