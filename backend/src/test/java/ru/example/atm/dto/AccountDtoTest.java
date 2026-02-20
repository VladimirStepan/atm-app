package ru.example.atm.dto;

import org.junit.jupiter.api.Test;
import ru.example.atm.enums.AccountStatus;
import ru.example.atm.enums.CurrencyCode;

import java.math.BigDecimal;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AccountDtoTest {

    @Test
    void accountCreateDtoShouldExposeFields() {
        AccountCreateDto dto = new AccountCreateDto("Ivan", new BigDecimal("500.00"), CurrencyCode.RUB);

        assertEquals("Ivan", dto.ownerName());
        assertEquals(new BigDecimal("500.00"), dto.balance());
        assertEquals(CurrencyCode.RUB, dto.currency());
    }

    @Test
    void accountUpdateDtoShouldExposeFields() {
        AccountUpdateDto dto = new AccountUpdateDto("Petr", new BigDecimal("1500.00"), CurrencyCode.USD, AccountStatus.FROZEN);

        assertEquals("Petr", dto.ownerName());
        assertEquals(new BigDecimal("1500.00"), dto.balance());
        assertEquals(CurrencyCode.USD, dto.currency());
        assertEquals(AccountStatus.FROZEN, dto.accountStatus());
    }

    @Test
    void accountResponseDtoShouldExposeFields() {
        Instant createdAt = Instant.parse("2025-01-01T10:15:30Z");
        Instant updatedAt = Instant.parse("2025-01-02T10:15:30Z");
        AccountResponseDto dto = new AccountResponseDto(
                11L, "Alex", new BigDecimal("42.00"), CurrencyCode.EUR, AccountStatus.ACTIVE, createdAt, updatedAt
        );

        assertEquals(11L, dto.id());
        assertEquals("Alex", dto.ownerName());
        assertEquals(new BigDecimal("42.00"), dto.balance());
        assertEquals(CurrencyCode.EUR, dto.currency());
        assertEquals(AccountStatus.ACTIVE, dto.accountStatus());
        assertEquals(createdAt, dto.createdAt());
        assertEquals(updatedAt, dto.updatedAt());
    }
}
