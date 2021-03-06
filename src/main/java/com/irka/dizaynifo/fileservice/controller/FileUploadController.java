package com.irka.dizaynifo.fileservice.controller;

import com.irka.common.enums.FileType;
import com.irka.common.model.FileUploadModel;
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

/*    @PostMapping(value = "/upload", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public BaseResponse<Long> uploadFile(@RequestPart("multipartFile") MultipartFile multipartFile,
                                         @RequestParam String fileType,
                                         @RequestParam String userId){
        FileUploadModel fileUploadModel = new FileUploadModel();
        fileUploadModel.setFileType(FileType.valueOf(fileType));
        fileUploadModel.setMultipartFile(multipartFile);
        fileUploadModel.setCategory(userId);
        return new BaseResponse<>(fileService.storeFile(fileUploadModel));
    }*/

    @GetMapping(value = "/download")
    public BaseResponse<byte[]> download(@RequestParam String fileId) throws IOException {
        return new BaseResponse<>(fileService.getFileById(fileId).getContent().getData());
    }

}
