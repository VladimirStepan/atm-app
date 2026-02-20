package ru.example.atm.dto;

import ru.example.atm.enums.AccountStatus;
import ru.example.atm.enums.CurrencyCode;

import java.math.BigDecimal;
import java.time.Instant;

public record AccountResponseDto(
        Long id,
        String ownerName,
        BigDecimal balance,
        CurrencyCode currency,
        AccountStatus accountStatus,
        Instant createdAt,
        Instant updatedAt) {
}
