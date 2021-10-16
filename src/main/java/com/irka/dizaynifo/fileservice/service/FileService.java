package com.irka.dizaynifo.fileservice.service;

import com.irka.common.enums.FileType;
import com.irka.common.model.DesignerModel;
import com.irka.common.model.FileUploadModel;
import com.irka.infrastructure.rest.BaseError;
import com.irka.infrastructure.rest.BaseException;
import com.irka.infrastructure.service.GenericService;
import com.irka.dizaynifo.fileservice.entity.FileEntity;
import com.irka.dizaynifo.fileservice.repository.FileRepository;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.gridfs.model.GridFSFile;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.bson.types.Binary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.mongodb.core.query.TextQuery;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.validation.constraints.NotNull;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Calendar;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileService {

    private final FileRepository fileRepository;

    public String storeFile(FileUploadModel fileUploadModel) throws IOException {
        byte[] designContent = fileUploadModel.getDesignContent();
        String designName = fileUploadModel.getDesignName();
        String designContentType = fileUploadModel.getContentType();
        DesignerModel designerModel = fileUploadModel.getDesignerModel();
        FileType fileType = fileUploadModel.getFileType();


        FileEntity fileEntity = new FileEntity();
        fileEntity.setFileName(designName);
        fileEntity.setFileType(fileType);
        fileEntity.setDocumentFormat(designContentType);
        fileEntity.setContent(new Binary(designContent));
        fileEntity.setSize(designContent.length);
        fileEntity.setUserId(designerModel.getId());

        return fileRepository.save(fileEntity).getId();
    }

    public FileEntity getFileById(String fileId){
        if (fileRepository.existsById(fileId)){
            return fileRepository.findById(fileId).orElseThrow();
        }
        throw new BaseException(BaseError.ENUM.GENERIC_ERROR);
    }
}
