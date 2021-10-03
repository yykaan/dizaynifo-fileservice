package com.irka.dizaynifo.fileservice.mapper;

import com.irka.common.model.FileModel;
import com.irka.infrastructure.mapper.BaseMapper;
import com.irka.dizaynifo.fileservice.entity.FileEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FileMapper extends BaseMapper<FileEntity, FileModel> {

}