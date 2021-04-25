package com.db.awmd.challenge;

import com.db.awmd.challenge.domain.Account;
import com.db.awmd.challenge.service.AccountsService;
import com.db.awmd.challenge.service.TransferService;
import com.db.awmd.challenge.service.ValidationService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
public class TransferControllerTest {
    private MockMvc mockMvc;

    @Autowired
    private AccountsService accountsService;

    @Autowired
    private TransferService transferService;

    @Autowired
    private ValidationService validationService;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before
    public void prepareMockMvc() {
        this.mockMvc = webAppContextSetup(this.webApplicationContext).build();

        // Reset the existing accounts before each test.
        accountsService.getAccountsRepository().clearAccounts();
    }

    @Test
    public void executeTransfer() throws Exception {
        String uniqueId = "Id-1001";
        Account account = new Account(uniqueId,new BigDecimal("220.45"));
        this.accountsService.createAccount(account);
        String uniqueAccountId2 = "Id-2002";
        Account account2 = new Account(uniqueAccountId2,new BigDecimal("100"));
        this.accountsService.createAccount(account2);
        BigDecimal amount = new BigDecimal("120.45");
        this.mockMvc.perform(get("/v1/transfer/Id-1001/Id-1002/"+ amount))
                .andExpect(status().isOk())
                .andExpect(
                        content().string("{\"accountId\":\"" + uniqueAccountId2 + "\",\"balance\":123.45}"));
        //this.transferService.transfer(account.getAccountId(),account2.getAccountId(),amount);
            }
}
