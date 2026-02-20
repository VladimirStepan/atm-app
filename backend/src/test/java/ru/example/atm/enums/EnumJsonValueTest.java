package ru.example.atm.enums;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EnumJsonValueTest {

    @Test
    void accountStatusShouldReturnLowercaseJsonValue() {
        assertEquals("active", AccountStatus.ACTIVE.toJson());
        assertEquals("frozen", AccountStatus.FROZEN.toJson());
        assertEquals("closed", AccountStatus.CLOSED.toJson());
    }

    @Test
    void currencyCodeShouldReturnLowercaseJsonValue() {
        assertEquals("rub", CurrencyCode.RUB.toJson());
        assertEquals("usd", CurrencyCode.USD.toJson());
        assertEquals("eur", CurrencyCode.EUR.toJson());
    }
}
