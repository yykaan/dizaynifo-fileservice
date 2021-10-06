package com.irka.dizaynifo.fileservice.service;

import com.irka.common.model.UnapprovedDesignModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class UnapprovedDesignSender {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${kafka.topics.unapproveddesign:unapproveddesign-upload}")
    private String topic;

    public void send(UnapprovedDesignModel unapprovedDesignModel) {
        kafkaTemplate.send(new ProducerRecord<>(topic, unapprovedDesignModel.getFileId().toString(), unapprovedDesignModel));
    }
}
