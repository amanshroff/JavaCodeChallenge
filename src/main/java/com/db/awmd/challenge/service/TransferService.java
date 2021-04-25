package com.db.awmd.challenge.service;

import com.db.awmd.challenge.domain.Account;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@AllArgsConstructor(onConstructor_ = {@Autowired})
@Service
public class TransferService {

    private TransactionService transactionService;
    private AccountsService bankAccountService;

    public void transfer(String fromBankAccountId, String toBankAccountId, BigDecimal amount) {
       Account fromBankAccount = bankAccountService.getAccount(fromBankAccountId);
       Account toBankAccount = bankAccountService.getAccount(toBankAccountId);

        transactionService.executeTransfer(fromBankAccount, toBankAccount, amount);
    }


}
