package com.irka.dizaynifo.fileservice.service;

import com.irka.common.enums.FileType;
import com.irka.common.model.DesignerModel;
import com.irka.common.model.FileUploadModel;
import com.irka.infrastructure.rest.BaseError;
import com.irka.infrastructure.rest.BaseException;
import com.irka.infrastructure.service.GenericService;
import com.irka.dizaynifo.fileservice.entity.FileEntity;
import com.irka.dizaynifo.fileservice.repository.FileRepository;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Calendar;
import java.util.Objects;
import java.util.UUID;

@Service
public class FileService extends GenericService<FileEntity> {
    private Path fileStorageLocation;
    private final FileRepository fileRepository;

    @Value("${file.upload.dir}")
    private String fileUploadDir;

    public FileService(FileRepository repository) {
        super(repository);
        this.fileRepository = repository;
    }

    @PostConstruct
    public void init(){
        this.fileStorageLocation = Paths.get(fileUploadDir).toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new BaseException(BaseError.ENUM.UPLOAD_FILE_DIRECTORY_CANNOT_BE_CREATED);
        }
    }

    private void createUserUploadDirectory(String userId){
        String year = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
        String month = String.valueOf(Calendar.getInstance().get(Calendar.MONTH));
        String day = String.valueOf(Calendar.getInstance().get(Calendar.DAY_OF_MONTH));

        this.fileStorageLocation = Paths.get(fileUploadDir + File.separator + userId + File.separator + year + File.separator + month + File.separator + day).toAbsolutePath().normalize();
        try {
            if (!Files.exists(this.fileStorageLocation)){
                Files.createDirectories(this.fileStorageLocation);
            }
        } catch (Exception ex) {
            throw new BaseException(BaseError.ENUM.UPLOAD_FILE_DIRECTORY_CANNOT_BE_CREATED);
        }
    }

    public Long storeFile(FileUploadModel fileUploadModel) throws IOException {
        byte[] designContent = fileUploadModel.getDesignContent();
        String designName = fileUploadModel.getDesignName();
        String designContentType = fileUploadModel.getContentType();
        DesignerModel designerModel = fileUploadModel.getDesignerModel();
        FileType fileType = fileUploadModel.getFileType();

        String userId = String.valueOf(designerModel.getId());
        createUserUploadDirectory(userId+"_"+designerModel.getUsername());



        FileEntity newDoc = new FileEntity();
        newDoc.setFileName(designName);
        newDoc.setFileType(fileType);
        newDoc.setDocumentFormat(designContentType);
        newDoc.setUploadDir(this.fileStorageLocation.toAbsolutePath().toString());
        Long fileId = fileRepository.save(newDoc).getId();
        FileUtils.writeByteArrayToFile(new File(this.fileStorageLocation.toAbsolutePath() + File.separator + fileId + designName), designContent);
        return fileId;
    }

    public FileEntity getFileById(Long fileId){
        if (fileRepository.existsById(fileId)){
            return fileRepository.getById(fileId);
        }
        throw new BaseException(BaseError.ENUM.GENERIC_ERROR);
    }


    public ResponseEntity<ByteArrayResource> downloadFile(Long fileId) throws IOException {
        FileEntity fileEntity = fileRepository.getById(fileId);
        File file = Paths.get(fileEntity.getUploadDir() + File.separator + fileEntity.getFileName()).toFile();

        Path path = Paths.get(file.getAbsolutePath());
        ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=myDoc.docx");
        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(file.length())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

    public byte[] getFileContent(Long fileId) throws IOException {
        FileEntity fileEntity = fileRepository.getById(fileId);
        InputStream in = new FileInputStream(Paths.get(fileEntity.getUploadDir() + File.separator + fileId + fileEntity.getFileName()).toAbsolutePath().normalize().toString());
        return IOUtils.toByteArray(in);
    }
}
