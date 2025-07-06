package com.bulkupload.controller;

import com.bulkupload.model.Student;
import com.bulkupload.service.BulkUploadService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/bulkuploads")
@RequiredArgsConstructor
public class BulkUploadController {

    private final BulkUploadService service;

    @PostMapping
    public ResponseEntity<?> bulkUpload(@RequestParam("file")MultipartFile file) throws IOException {
        service.bulkUpload(file);
        return new ResponseEntity<>("message"+"bulk upload successsfullly done" , HttpStatus.OK);
    }
}
