package com.bank.consumer.service;

import com.bank.consumer.dao.AccountDao;
import com.bank.consumer.dao.TransferDao;
import com.bank.common.model.Account;
import com.bank.common.model.Transfer;
import com.bank.common.model.TransferMessage;
import com.bank.common.model.TransferStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.bind.ValidationException;
import java.math.BigDecimal;
import java.util.UUID;

@Service
public class TransferProcessingService {
    private static final Logger logger = LogManager.getLogger(TransferProcessingService.class);

    @Autowired
    private AccountDao accountDao;

    @Autowired
    private TransferDao transferDao;

    @Transactional(rollbackFor = Exception.class)
    public void processTransfer(TransferMessage message) throws ValidationException {
        logger.info("Processing transfer: id={}, from={}, to={}, amount={}",
                message.getTransferId(),
                message.getFromAccountId(),
                message.getToAccountId(),
                message.getAmount());

        try {
            Account fromAccount = accountDao.findById(message.getFromAccountId());
            Account toAccount = accountDao.findById(message.getToAccountId());

            BigDecimal newFromBalance = fromAccount.getBalance().subtract(message.getAmount());
            BigDecimal newToBalance = toAccount.getBalance().add(message.getAmount());

            fromAccount.setBalance(newFromBalance);
            toAccount.setBalance(newToBalance);

            accountDao.update(fromAccount);
            accountDao.update(toAccount);

            Transfer transfer = createTransfer(message, TransferStatus.DONE);
            transferDao.save(transfer);

            logger.info("Transfer processed successfully: id={}", message.getTransferId());
        } catch (Exception e) {
            logger.error("Transaction failed for transfer id={}: {}",
                    message.getTransferId(), e.getMessage());

            createErrorTransferRecord(message);

            throw e;
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void createErrorTransferRecord(TransferMessage message) {
        try {
            Transfer transfer = createTransfer(message, TransferStatus.ERROR);
            transferDao.save(transfer);
            logger.info("Error transfer record created successfully for id={}", message.getTransferId());
        } catch (Exception e) {
            logger.error("Failed to create error transfer record for id={}: {}",
                    message.getTransferId(), e.getMessage());
        }
    }

    private Transfer createTransfer(TransferMessage message, TransferStatus status) {
        Transfer transfer = new Transfer();
        transfer.setId(UUID.randomUUID().toString());
        transfer.setFromAccountId(message.getFromAccountId());
        transfer.setToAccountId(message.getToAccountId());
        transfer.setAmount(message.getAmount());
        transfer.setStatus(status);
        return transfer;
    }
}