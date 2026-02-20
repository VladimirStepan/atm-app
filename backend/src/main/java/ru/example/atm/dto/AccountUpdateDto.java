package ru.example.atm.dto;

import ru.example.atm.enums.AccountStatus;
import ru.example.atm.enums.CurrencyCode;

import java.math.BigDecimal;

public record AccountUpdateDto(
        String ownerName,
        BigDecimal balance,
        CurrencyCode currency,
        AccountStatus accountStatus) {
}
