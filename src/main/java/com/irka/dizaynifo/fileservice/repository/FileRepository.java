package com.irka.dizaynifo.fileservice.repository;

import com.irka.infrastructure.repository.GenericRepository;
import com.irka.dizaynifo.fileservice.entity.FileEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepository extends GenericRepository<FileEntity> {

}
