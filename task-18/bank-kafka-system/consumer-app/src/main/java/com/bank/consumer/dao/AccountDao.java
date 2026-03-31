package com.bank.consumer.dao;

import com.bank.common.dao.AbstractDao;
import com.bank.common.model.Account;
import org.springframework.stereotype.Repository;

@Repository
public class AccountDao extends AbstractDao<Account, Long> {
    public AccountDao() {
        super();
    }
}