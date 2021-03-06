package com.db.awmd.challenge.service;

import com.db.awmd.challenge.domain.Account;
import com.db.awmd.challenge.repository.AccountsRepository;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class AccountsService {

  @Getter
  private final AccountsRepository accountsRepository;

  @Autowired
  public AccountsService(AccountsRepository accountsRepository) {
    this.accountsRepository = accountsRepository;
  }

  public void createAccount(Account account) {
    this.accountsRepository.createAccount(account);
  }

  public Account getAccount(String accountId) {
    return this.accountsRepository.getAccount(accountId);
  }

  public void decreaseCurrentBalance(Account bankAccount, BigDecimal amount) {
    BigDecimal updatedAmount = bankAccount.getBalance().subtract(amount);
    bankAccount.setBalance(updatedAmount);
  }

  public void increaseCurrentBalance(Account bankAccount, BigDecimal amount) {
    BigDecimal updatedAmount = bankAccount.getBalance().add(amount);
    bankAccount.setBalance(updatedAmount);

  }
}
