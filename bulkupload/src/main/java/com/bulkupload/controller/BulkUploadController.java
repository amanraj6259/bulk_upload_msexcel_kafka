package com.bulkupload.controller;

import com.bulkupload.service.BulkUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/bulkuploads")
@RequiredArgsConstructor
public class BulkUploadController {

    private final BulkUploadService service;

    @PostMapping("/{orgId}")
    public ResponseEntity<?> bulkUpload(
            @RequestParam("file")MultipartFile file,
            @PathVariable("orgId")int org
    )
            throws IOException {
        service.bulkUpload(file,org);
        return new ResponseEntity<>("message"+"bulk upload successsfullly done" , HttpStatus.OK);
    }
}
