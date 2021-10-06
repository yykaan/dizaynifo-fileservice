package com.irka.dizaynifo.fileservice.service;

import com.irka.common.model.FileUploadModel;
import com.irka.common.model.UnapprovedDesignModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class DesignUploadListener {
    private final FileService fileService;
    private final UnapprovedDesignSender unapprovedDesignSender;

    @Value("${kafka.topics.design.name}")
    private String topic;

    @KafkaListener(topics = "${kafka.topics.design.name}", groupId = "${kafka.topics.design.consumer.groupId}")
    public void listenDesignUpload(List<FileUploadModel> fileUploadModelList) {
        log.info("Consuming message from topic: {}. Batch size: {}", topic, fileUploadModelList.size());
        for (FileUploadModel fileUploadModel : fileUploadModelList){
            Long fileId;
            try {
                fileId = fileService.storeFile(fileUploadModel);
                UnapprovedDesignModel unapprovedDesignModel = new UnapprovedDesignModel();
                unapprovedDesignModel.setFileId(fileId);
                unapprovedDesignModel.setDesigner(fileUploadModel.getDesignerModel());
                unapprovedDesignModel.setCategory(fileUploadModel.getCategory());
                unapprovedDesignModel.setDesignName(fileUploadModel.getDesignName());
                unapprovedDesignModel.setCategory(fileUploadModel.getCategory());
                unapprovedDesignSender.send(unapprovedDesignModel);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
