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
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
        this.mockMvc.perform(post("/v1/accounts").contentType(MediaType.APPLICATION_JSON)
                .content("{\"accountId\":\"Id-123\",\"balance\":1000}")).andExpect(status().isCreated());
        this.mockMvc.perform(post("/v1/accounts").contentType(MediaType.APPLICATION_JSON)
                .content("{\"accountId\":\"Id-456\",\"balance\":1000}")).andExpect(status().isCreated());
        this.mockMvc.perform(post("/v1/transfer").contentType(MediaType.APPLICATION_JSON)
                .content("{\"fromBankAccountId\":\"Id-123\",\"toBankAccountId\":\"Id-456\",\"amount\":1200}")).andExpect(status().isBadRequest());
    }
}
