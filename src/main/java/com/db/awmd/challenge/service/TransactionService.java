package com.db.awmd.challenge.service;

import com.db.awmd.challenge.domain.Account;
import com.db.awmd.challenge.exception.InsufficientBalanceManagerException;
import com.db.awmd.challenge.repository.AccountsRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Optional;

@AllArgsConstructor(onConstructor_ = {@Autowired})
@Service
public class TransactionService {
    private ValidationService validationService;
    private AccountsService accountsService;
    private AccountsRepository accountsRepository;
    //private EmailNotificationService emailNotificationService;

    public void   executeTransfer(Account fromBankAccount, Account toBankAccount, final BigDecimal amount) throws InsufficientBalanceManagerException {
        // validate parameters
        ValidationService.checkNotNull(fromBankAccount, "fromBankAccount can not be null");
        ValidationService.checkNotNull(toBankAccount, "toBankAccount can not be null");
        ValidationService.checkArgument(!Objects.equals(fromBankAccount.getAccountId(), toBankAccount.getAccountId()),
                "Transfer can not executed an account to the same account. bankAccountId: ",
                fromBankAccount.getAccountId());

        synchronized(this) {
            validationService.validAmount(amount);
            takeMoney(fromBankAccount, amount);
            putMoney(toBankAccount, amount);
        }

    }
    private void takeMoney(Account bankAccount, BigDecimal amount) {

        validationService.checkWithdrawable(bankAccount, amount);
        accountsService.decreaseCurrentBalance(bankAccount, amount);

    }

    private void putMoney(Account bankAccount, BigDecimal amount) {

        accountsService.increaseCurrentBalance(bankAccount, amount);


    }

}
