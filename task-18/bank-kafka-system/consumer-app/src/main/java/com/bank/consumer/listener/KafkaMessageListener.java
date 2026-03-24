package com.bank.consumer.listener;

import com.bank.common.model.TransferMessage;
import com.bank.common.util.JsonUtil;
import com.bank.consumer.service.TransferProcessingService;
import javax.xml.bind.ValidationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class KafkaMessageListener {
    private static final Logger logger = LogManager.getLogger(KafkaMessageListener.class);

    @Autowired
    private TransferProcessingService transferProcessingService;

    @KafkaListener(
            topics = "bank-transfers",
            groupId = "${kafka.consumer.group.id}",
            containerFactory = "batchContainerFactory"
    )
    public void listenBatch(
            @Payload List<String> messages,
            @Header(KafkaHeaders.RECEIVED_PARTITION_ID) List<Integer> partitions,
            @Header(KafkaHeaders.OFFSET) List<Long> offsets,
            Acknowledgment ack
    ) {
        logger.info("Received batch of {} messages", messages.size());

        for (int i = 0; i < messages.size(); i++) {
            String json = messages.get(i);
            Integer partition = partitions.get(i);
            Long offset = offsets.get(i);

            logger.info("Start processing message: partition={}, offset={}", partition, offset);

            try {
                TransferMessage message = JsonUtil.fromJson(json, TransferMessage.class);
                transferProcessingService.processTransfer(message);
                logger.info("Successfully processed transfer id={}", message.getTransferId());
            } catch (ValidationException e) {
                logger.error("Validation failed for message partition={}, offset={}: {}",
                        partition, offset, e.getMessage());
            } catch (Exception e) {
                logger.error("Transaction failed for message partition={}, offset={}: {}",
                        partition, offset, e.getMessage());
            }
        }

        ack.acknowledge();
    }
}