package com.pgr301.exam;

import com.pgr301.exam.model.Account;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class BankAccountApplicationTest {

    @Test
    void easyTestToBreak() {

        assertEquals("DevOps", "TeamDino");  //To Break the test, use this instead

    }

    @Test
    public void MakeNewAccountAndGetAccountBalance() {

        Account testAccount = new Account();

        BigDecimal balance = new BigDecimal(1337);

        testAccount.setBalance(balance);

        assertEquals(balance, testAccount.getBalance());

    }

}
