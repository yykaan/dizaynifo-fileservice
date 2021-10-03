package com.irka.dizaynifo.fileservice.controller;

import com.irka.infrastructure.rest.BaseResponse;
import com.irka.dizaynifo.fileservice.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
public class FileUploadController {

    private final FileService fileService;

    @PostMapping(value = "/upload", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public BaseResponse<Long> uploadFile(@RequestPart("multipartFile") MultipartFile multipartFile,
                                         @RequestParam String fileType,
                                         @RequestParam String userId){
        return new BaseResponse<>(fileService.storeFile(multipartFile,fileType, userId));
    }

    @GetMapping(value = "/download")
    public BaseResponse<byte[]> upload(@RequestParam Long fileId) throws IOException {
        return new BaseResponse<>(fileService.getFileContent(fileId));
    }

}
