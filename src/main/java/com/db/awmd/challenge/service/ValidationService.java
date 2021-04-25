package com.db.awmd.challenge.service;

import com.db.awmd.challenge.domain.Account;
import com.db.awmd.challenge.exception.InsufficientBalanceManagerException;
import com.db.awmd.challenge.repository.AccountsRepository;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@AllArgsConstructor(onConstructor_ = {@Autowired})
@Service
public class ValidationService {
    private AccountsRepository accountsRepository;

    public static <T> T checkNotNull(T reference, @Nullable Object errorMessage) {
        if (reference == null) {
            throw new NullPointerException(String.valueOf(errorMessage));
        }
        return reference;
    }

    public static void checkArgument(
            boolean b, @Nullable String errorMessageTemplate, @Nullable Object p1) {
        if (!b) {
            throw new IllegalArgumentException(format(errorMessageTemplate, p1));
        }
    }

    public static void checkArgument(boolean expression, @Nullable Object errorMessage) {
        if (!expression) {
            throw new IllegalArgumentException(String.valueOf(errorMessage));
        }
    }

    public void validAmount(BigDecimal amount) {
        checkNotNull(amount, "amount can not be null");
        checkArgument(!isNegative(amount), "amount can not be negative");
    }

    public static boolean isNegative(BigDecimal amount) {
        return BigDecimal.ZERO.compareTo(amount) > 0;
    }

    public void checkWithdrawable(Account bankAccount, BigDecimal amount) {
        if (isNegative(bankAccount.getBalance().subtract(amount))) {
            throw InsufficientBalanceManagerException.to(
                    "Account current balance is not available to withdraw. current balance: %s, amount: %s",
                    bankAccount.getBalance(),
                    amount);
        }
    }

    static String format(String template, @Nullable Object... args) {
        template = String.valueOf(template); // null -> "null"

        // start substituting the arguments into the '%s' placeholders
        StringBuilder builder = new StringBuilder(template.length() + 16 * args.length);
        int templateStart = 0;
        int i = 0;
        while (i < args.length) {
            int placeholderStart = template.indexOf("%s", templateStart);
            if (placeholderStart == -1) {
                break;
            }
            builder.append(template, templateStart, placeholderStart);
            builder.append(args[i++]);
            templateStart = placeholderStart + 2;
        }
        builder.append(template, templateStart, template.length());

        // if we run out of placeholders, append the extra args in square braces
        if (i < args.length) {
            builder.append(" [");
            builder.append(args[i++]);
            while (i < args.length) {
                builder.append(", ");
                builder.append(args[i++]);
            }
            builder.append(']');
        }

        return builder.toString();
    }

}
