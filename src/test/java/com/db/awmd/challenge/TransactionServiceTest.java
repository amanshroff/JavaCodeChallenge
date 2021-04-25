package com.db.awmd.challenge;

import com.db.awmd.challenge.domain.Account;
import com.db.awmd.challenge.exception.InsufficientBalanceManagerException;
import com.db.awmd.challenge.service.AccountsService;
import com.db.awmd.challenge.service.TransactionService;
import com.db.awmd.challenge.service.ValidationService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TransactionServiceTest {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private AccountsService accountsService;

    @Autowired
    private ValidationService validationService;


    @Test
    public void testWithdrawInsufficientBalanceException() {

        String uniqueId = "Id-1" ;
        Account account1 = new Account(uniqueId);
        account1.setBalance(new BigDecimal(1000));
        uniqueId = "Id-2";
        Account account2 = new Account(uniqueId);
        account1.setBalance(new BigDecimal(1000));

        this.accountsService.createAccount(account1);
        this.accountsService.createAccount(account2);
        BigDecimal amount = new BigDecimal(2000);
        try {
            this.transactionService.executeTransfer(account1,account2,amount);
            fail("Should have failed when amount is greater than current balance");
        } catch (InsufficientBalanceManagerException ex) {
            assertThat(ex.getMessage()).isEqualTo("Account current balance is not available to withdraw. current balance: "+account1.getBalance()+", amount: "+amount);
        }
    }

}
