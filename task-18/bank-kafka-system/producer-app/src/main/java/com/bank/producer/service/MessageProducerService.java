package com.bank.producer.service;

import com.bank.common.model.Account;
import com.bank.common.model.TransferMessage;
import com.bank.common.util.JsonUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Random;

@Service
public class MessageProducerService {
    private static final Logger logger = LogManager.getLogger(MessageProducerService.class);

    @Autowired
    private AccountService accountService;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    private final Random random = new Random();

    private boolean isProcessing = false;

    @Scheduled(fixedRate = 1000, initialDelay = 1000)
    @Transactional(readOnly = true)
    public synchronized void generateAndSendMessages() {
        if (isProcessing) {
            logger.debug("Previous iteration still running, skipping this tick");
            return;
        }

        isProcessing = true;
        try {
            for (int i = 0; i < 5; i++) {
                generateAndSendMessage();
            }
        } finally {
            isProcessing = false;
        }
    }

    private void generateAndSendMessage() {
        List<Account> accounts = accountService.getAllAccounts();
        if (accounts.size() < 2) {
            logger.error("Not enough accounts to generate transfer");
            return;
        }

        Account fromAccount = accounts.get(random.nextInt(accounts.size()));
        Account toAccount = accounts.get(random.nextInt(accounts.size()));

        while (fromAccount.getId().equals(toAccount.getId())) {
            toAccount = accounts.get(random.nextInt(accounts.size()));
        }

        BigDecimal amount = new BigDecimal(random.nextInt(1000) + 1)
                .setScale(2, RoundingMode.HALF_UP);

        TransferMessage message = new TransferMessage(
                fromAccount.getId(),
                toAccount.getId(),
                amount
        );

        String json = JsonUtil.toJson(message);

        logger.info("Generating transfer message: id={}, from={}, to={}, amount={}",
                message.getTransferId(),
                fromAccount.getId(),
                toAccount.getId(),
                amount);

        kafkaTemplate.send("bank-transfers", message.getTransferId(), json);

        logger.info("Message sent to Kafka: id={}", message.getTransferId());
    }
}