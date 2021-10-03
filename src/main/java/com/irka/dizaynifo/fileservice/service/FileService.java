package com.irka.dizaynifo.fileservice.service;

import com.irka.common.enums.FileType;
import com.irka.infrastructure.rest.BaseError;
import com.irka.infrastructure.rest.BaseException;
import com.irka.infrastructure.service.GenericService;
import com.irka.dizaynifo.fileservice.entity.FileEntity;
import com.irka.dizaynifo.fileservice.repository.FileRepository;
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

    public Long storeFile(MultipartFile multipartFile,
                          String fileType,
                          String userId) {
        createUserUploadDirectory(userId);
        String originalFileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        String fileName;
        try {
            if(originalFileName.contains("..")) {
                throw new BaseException(BaseError.ENUM.UPLOAD_FILE_DIRECTORY_CANNOT_BE_CREATED);
            }

            FileEntity newDoc = new FileEntity();
            newDoc.setDocumentFormat(multipartFile.getContentType());
            newDoc.setFileName(userId+"_"+multipartFile.getOriginalFilename());
            newDoc.setFileType(FileType.valueOf(fileType));
            newDoc.setUploadDir(this.fileStorageLocation.toAbsolutePath().toString());
            Long fileId = fileRepository.save(newDoc).getId();

            fileName = userId+"_"+multipartFile.getOriginalFilename();
            Path targetLocation = this.fileStorageLocation.resolve(fileName);

            Files.copy(multipartFile.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return fileId;
        } catch (IOException ex) {
            throw new BaseException(BaseError.ENUM.UPLOAD_FILE_DIRECTORY_CANNOT_BE_CREATED);
        }
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
        InputStream in = new FileInputStream(Paths.get(fileEntity.getUploadDir() + File.separator + fileEntity.getFileName()).toAbsolutePath().normalize().toString());
        return IOUtils.toByteArray(in);
    }
}
