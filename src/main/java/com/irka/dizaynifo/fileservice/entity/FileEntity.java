package com.irka.dizaynifo.fileservice.entity;

import com.irka.common.enums.FileType;
import com.irka.infrastructure.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.Binary;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Document
public class FileEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private String id;

    @Version
    private Integer version;

    @CreatedBy
    private String createdBy;

    @CreatedDate
    private Date createdDate;

    @LastModifiedBy
    private String modifiedBy;

    @LastModifiedDate
    private Date updatedDate;

    private boolean deleted = false;

    private String fileName;

    private String documentFormat;

    @Enumerated(EnumType.STRING)
    private FileType fileType;

    private Binary content;

    private long size;

    private long userId;
}
