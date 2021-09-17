package com.irka.tasarlasat.fileservice.repository;

import com.irka.infrastructure.repository.GenericRepository;
import com.irka.tasarlasat.fileservice.entity.FileEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepository extends GenericRepository<FileEntity> {

}
