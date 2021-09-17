package com.irka.tasarlasat.fileservice.controller;

import com.irka.infrastructure.rest.BaseResponse;
import com.irka.tasarlasat.fileservice.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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

}
