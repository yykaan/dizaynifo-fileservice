package com.irka.dizaynifo.fileservice.repository;

import com.irka.dizaynifo.fileservice.entity.FileEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepository extends MongoRepository<FileEntity, String> {

}
