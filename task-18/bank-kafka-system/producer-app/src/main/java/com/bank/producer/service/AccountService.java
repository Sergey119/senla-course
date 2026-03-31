package com.bank.producer.service;

import com.bank.producer.dao.AccountDao;
import com.bank.common.model.Account;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AccountService {
    private static final Logger logger = LogManager.getLogger(AccountService.class);
    private static final int INITIAL_ACCOUNTS_COUNT = 1000;
    private static final BigDecimal INITIAL_BALANCE = new BigDecimal("10000.00");

    @Autowired
    private AccountDao accountDao;

    private final Map<Long, Account> accountsCache = new HashMap<>();

    private final Object lock = new Object();

    private volatile boolean initialized = false;

    @Transactional
    public void initializeAccounts() {
        if (initialized) {
            logger.info("Accounts already initialized, skipping");
            return;
        }

        synchronized (lock) {
            if (initialized) {
                return;
            }

            List<Account> existingAccounts = accountDao.findAll();
            if (existingAccounts.isEmpty()) {
                logger.info("No accounts found in DB, generating {} accounts", INITIAL_ACCOUNTS_COUNT);
                generateAndSaveAccounts();
            } else {
                logger.info("Found {} existing accounts in DB, loading to cache", existingAccounts.size());
                loadAccountsToCache(existingAccounts);
            }

            initialized = true;
            logger.info("Accounts initialization completed, total accounts in cache: {}", accountsCache.size());
        }
    }

    @Transactional(readOnly = true)
    public List<Account> getAllAccounts() {
        if (!initialized) {
            logger.warn("Accounts not initialized yet, loading from DB");
            synchronized (lock) {
                if (!initialized) {
                    List<Account> accounts = accountDao.findAll();
                    loadAccountsToCache(accounts);
                    initialized = true;
                }
            }
        }

        List<Account> result;
        synchronized (lock) {
            result = new ArrayList<>(accountsCache.values());
        }
        return result;
    }

    private void generateAndSaveAccounts() {
        for (int i = 0; i < INITIAL_ACCOUNTS_COUNT; i++) {
            Account account = new Account(INITIAL_BALANCE);
            accountDao.save(account);

            synchronized (lock) {
                accountsCache.put(account.getId(), account);
            }

            if (i == (INITIAL_ACCOUNTS_COUNT-1)) {
                logger.info("Generated {} accounts", i + 1);
            }
        }
    }

    private void loadAccountsToCache(List<Account> accounts) {
        synchronized (lock) {
            for (Account account : accounts) {
                accountsCache.put(account.getId(), account);
            }
        }
    }
}