package com.bank.consumer.dao;

import com.bank.common.dao.AbstractDao;
import com.bank.common.model.Transfer;
import org.springframework.stereotype.Repository;

@Repository
public class TransferDao extends AbstractDao<Transfer, String> {
    public TransferDao() {
        super();
    }
}