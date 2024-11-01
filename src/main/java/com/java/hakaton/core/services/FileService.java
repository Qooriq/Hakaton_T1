package com.java.hakaton.core.services;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FileService {

    @Value("./files")
    private String files;

    @SneakyThrows
    public void uploadFile(String filePath, InputStream inputStream) {
        Path path = Path.of(files, filePath);
        try (inputStream) {
            Files.createDirectories(path.getParent());
            Files.write(path, inputStream.readAllBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        }
    }

    @SneakyThrows
    public Optional<byte[]> getFile(String fileName) {
        Path path = Path.of(files, fileName);

        return Files.exists(path) ? Optional.of(Files.readAllBytes(path)) : Optional.empty();
    }




}
