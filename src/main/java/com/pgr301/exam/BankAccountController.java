package com.pgr301.exam;

import com.pgr301.exam.model.Account;
import com.pgr301.exam.model.Transaction;

import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.MeterRegistry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static java.util.Optional.ofNullable;

@RestController
public class BankAccountController implements ApplicationListener<ApplicationReadyEvent> {

    private Logger logger = LoggerFactory.getLogger(BankAccountController.class);

    @Autowired
    private BankingCoreSystmeService bankService;

    @Autowired
    private MeterRegistry meterRegistry;

    @Autowired
    public BankAccountController(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    @Timed("timed_transfer")
    @PostMapping(path = "/account/{fromAccount}/transfer/{toAccount}", consumes = "application/json", produces = "application/json")
    public void transfer(@RequestBody Transaction tx, @PathVariable String fromAccount, @PathVariable String toAccount) {
        meterRegistry.counter("transfer", "amount", String.valueOf(tx.getAmount()) ).increment();
        try{
            bankService.transfer(tx, fromAccount, toAccount);
            meterRegistry.counter("successful_transfer").increment();
            logger.info("Money transfer from " + fromAccount + " to " + toAccount);
        } catch (Exception e) {
            logger.info("BACKEND EXCEPTION while trying to transfer from " + fromAccount + " to " + toAccount);
            meterRegistry.counter("backend_exception").increment();
        }
    }

    @Timed("timed_account")
    @PostMapping(path = "/account", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Account> updateAccount(@RequestBody Account a) {
        logger.info("Account update/create " + a.getId());
        meterRegistry.counter("update_account").increment();
        bankService.updateAccount(a);
        return new ResponseEntity<>(a, HttpStatus.OK);
    }
    @Timed("timed_balance")
    @GetMapping(path = "/account/{accountId}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Account> balance(@PathVariable String accountId) {
        logger.info("Account balance request " + accountId);
        meterRegistry.counter("balance").increment();
        Account account = ofNullable(bankService.getAccount(accountId)).orElseThrow(AccountNotFoundException::new);
        meterRegistry.gauge("account_balance", account.getBalance());
        return new ResponseEntity<>(account, HttpStatus.OK);
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
    }

    @ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "video not found")
    public static class AccountNotFoundException extends RuntimeException {
    }
}
