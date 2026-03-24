package com.bank.common.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

public class TransferMessage implements Serializable {
    private String transferId;
    private Long fromAccountId;
    private Long toAccountId;
    private BigDecimal amount;

    public TransferMessage() {}

    public TransferMessage(Long fromAccountId, Long toAccountId, BigDecimal amount) {
        this.transferId = UUID.randomUUID().toString();
        this.fromAccountId = fromAccountId;
        this.toAccountId = toAccountId;
        this.amount = amount;
    }

    public String getTransferId() {
        return transferId;
    }
    public void setTransferId(String transferId) {
        this.transferId = transferId;
    }

    public Long getFromAccountId() {
        return fromAccountId;
    }
    public void setFromAccountId(Long fromAccountId) {
        this.fromAccountId = fromAccountId;
    }

    public Long getToAccountId() {
        return toAccountId;
    }
    public void setToAccountId(Long toAccountId) {
        this.toAccountId = toAccountId;
    }

    public BigDecimal getAmount() {
        return amount;
    }
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "TransferMessage{" +
                "transferId='" + transferId + '\'' +
                ", fromAccountId=" + fromAccountId +
                ", toAccountId=" + toAccountId +
                ", amount=" + amount +
                '}';
    }
}