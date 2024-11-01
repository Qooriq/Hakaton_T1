package com.java.hakaton.controller;

import com.java.hakaton.SocketConnectionService;
import com.java.hakaton.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class FileUploadController {

    private final FileService fileService;

    private final SocketConnectionService socketConnectionService;

    @PostMapping
    @SneakyThrows
    @RequestMapping(value = "/upload")
    public @ResponseBody ResponseEntity<String> handleFileUpload(@RequestParam("file") MultipartFile file) {
        if (!file.isEmpty()) {
            fileService.uploadFile(file.getName(), file.getInputStream());
            return ResponseEntity.ok("File uploaded successfully " + file.getName());
        } else {
            return ResponseEntity.badRequest().body("File is empty");
        }

    }

    @SneakyThrows
    @GetMapping(value = "/get")
    public @ResponseBody ResponseEntity<String> handleFileGet(@RequestParam String name) {
        var file = fileService.getFile(name);
        if (file.isPresent()) {
            String fileContent = new String(file.get());
            return new ResponseEntity<>(fileContent, HttpStatus.OK);
        } else {
            return ResponseEntity.badRequest().body("File not found");
        }
    }

    @PostMapping
    @SneakyThrows
    @RequestMapping(value = "/uploadDb")
    public @ResponseBody ResponseEntity<String> handleConnection(@RequestParam String dbIndicator, @RequestParam String indicator,
                                                                 @RequestParam String dbIpAddress,
                                                                 @RequestParam int start, @RequestParam int end,
                                                                 @RequestParam String[] fields,
                                                                 @RequestParam String url, @RequestParam String dbName,
                                                                 @RequestParam String username, @RequestParam String password) {
        socketConnectionService.sendDataToNodes(dbIndicator, indicator, dbIpAddress, start, end, fields, url, dbName, username, password);
        return ResponseEntity.ok("Connection established");
    }


}
