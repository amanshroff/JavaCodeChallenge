package com.db.awmd.challenge.web;

import com.db.awmd.challenge.domain.Transfer;
import com.db.awmd.challenge.exception.InsufficientBalanceManagerException;
import com.db.awmd.challenge.service.AccountsService;
import com.db.awmd.challenge.service.EmailNotificationService;
import com.db.awmd.challenge.service.TransferService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;

@AllArgsConstructor(onConstructor_ = {@Autowired})
@RestController
@RequestMapping(TransferController.SERVICE_PATH)
@Slf4j
public class TransferController {
    public static final String SERVICE_PATH = "/v1/transfer";

    private TransferService transferService;
    private EmailNotificationService emailNotificationService;
    private AccountsService accountsService;

    @PostMapping (consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> transfer(@RequestBody @Valid Transfer transfer) {
        log.info("/{}/{}/{} called with amount: {}", "", transfer.getFromBankAccountId(), transfer.getToBankAccountId(), transfer.getAmount());

        try {
            transferService.transfer(transfer.getFromBankAccountId(), transfer.getToBankAccountId(), transfer.getAmount());
        } catch (InsufficientBalanceManagerException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }

        emailNotificationService.notifyAboutTransfer(accountsService.getAccount(transfer.getFromBankAccountId()), "Amount = " + transfer.getAmount() + "Transferred to " + transfer.getToBankAccountId());
        emailNotificationService.notifyAboutTransfer(accountsService.getAccount(transfer.getToBankAccountId()), "Amount = " + transfer.getAmount() + "Transferred from " + transfer.getFromBankAccountId());

        return new ResponseEntity<>(HttpStatus.CREATED);

    }
}
