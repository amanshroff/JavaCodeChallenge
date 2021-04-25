package com.db.awmd.challenge.web;

import com.db.awmd.challenge.exception.InsufficientBalanceManagerException;
import com.db.awmd.challenge.service.AccountsService;
import com.db.awmd.challenge.service.EmailNotificationService;
import com.db.awmd.challenge.service.TransferService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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

    @PostMapping (path = "/{fromBankAccountId}/{toBankAccountId}/{amount}")
    public ResponseEntity<Object> transfer(@PathVariable(name = "fromBankAccountId") String fromBankAccountId,
                                           @PathVariable(name = "toBankAccountId") String toBankAccountId,
                                           @PathVariable (name = "amount") BigDecimal amount) {
        log.info("/{}/{}/{} called with amount: {}", "", fromBankAccountId, toBankAccountId, amount);

        try {
            transferService.transfer(fromBankAccountId, toBankAccountId, amount);
        } catch (InsufficientBalanceManagerException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }

        emailNotificationService.notifyAboutTransfer(accountsService.getAccount(fromBankAccountId), "Amount = " + amount + "Transferred to " + toBankAccountId);
        emailNotificationService.notifyAboutTransfer(accountsService.getAccount(toBankAccountId), "Amount = " + amount + "Transferred from " + fromBankAccountId);

        return new ResponseEntity<>(HttpStatus.CREATED);

    }

   /* @PostMapping (path = "/{fromBankAccountId}/{toBankAccountId}/{amount}")
    @ResponseStatus(value = HttpStatus.OK)
    public void transfer(@PathVariable(name = "fromBankAccountId") String fromBankAccountId,
                         @PathVariable(name = "toBankAccountId") String toBankAccountId,
                         @PathVariable (name = "amount")BigDecimal amount) {
        log.info("/{}/{}/{} called with amount: {}", SERVICE_PATH, fromBankAccountId, toBankAccountId, amount);

        transferService.transfer(fromBankAccountId, toBankAccountId, amount);

        emailNotificationService.notifyAboutTransfer(accountsService.getAccount(fromBankAccountId), "Amount = " + amount + "Transferred to " + toBankAccountId);
        emailNotificationService.notifyAboutTransfer(accountsService.getAccount(toBankAccountId), "Amount = " + amount + "Transferred from " + fromBankAccountId);

    }*/
}
