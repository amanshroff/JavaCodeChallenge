package com.db.awmd.challenge.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class Transfer {
    @NotNull
    @NotEmpty
    private final String fromBankAccountId;

    @NotNull
    @NotEmpty
    private final String toBankAccountId;

    @NotNull
    @Min(value = 0, message = "Transfer amount must be positive")
    private BigDecimal amount;


    @JsonCreator
    public Transfer(@JsonProperty("fromBankAccountId") String fromBankAccountId,
                    @JsonProperty("toBankAccountId") String toBankAccountId,
                   @JsonProperty("amount") BigDecimal amount) {
        this.fromBankAccountId = fromBankAccountId;
        this.toBankAccountId = toBankAccountId;
        this.amount = amount;
    }
}
