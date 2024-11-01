package com.java.hakaton.core.controllers;

import com.java.hakaton.core.services.SocketConnectionService;
import com.java.hakaton.core.services.FileService;
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

    @SneakyThrows
    @PostMapping(value = "/uploadDb")
    public @ResponseBody ResponseEntity<String> handleConnection(@RequestParam String dbIndicator, @RequestParam String indicator,
                                                                 @RequestParam int port,
                                                                 @RequestParam String dbIpAddress, @RequestParam String dbName, @RequestParam String tableName,
                                                                 @RequestParam String username, @RequestParam String password) {
/*        socketConnectionService.sendDataToNodes(dbIpAddress, port, tableName, username, password, dbName, dbIndicator, indicator);*/
        socketConnectionService.sendDataToNodes("10.1.44.64", 5432 , "users", "wayzap", "F872G5g778M", "hakaton", "0", "1");
        return ResponseEntity.ok("Connection established");
    }


}
