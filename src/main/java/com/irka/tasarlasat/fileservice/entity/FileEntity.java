package com.irka.tasarlasat.fileservice.entity;

import com.irka.common.enums.FileType;
import com.irka.infrastructure.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import javax.persistence.*;

@Audited
@Getter
@Setter
@Entity
@Table(name = "FILE")
public class FileEntity extends BaseEntity {

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "document_format")
    private String documentFormat;

    @Column(name = "upload_dir")
    private String uploadDir;

    @Enumerated(EnumType.STRING)
    @Column(name = "file_type")
    private FileType fileType;
}
