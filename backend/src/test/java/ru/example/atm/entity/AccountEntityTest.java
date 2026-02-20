package ru.example.atm.entity;

import org.junit.jupiter.api.Test;
import ru.example.atm.enums.AccountStatus;
import ru.example.atm.enums.CurrencyCode;

import java.math.BigDecimal;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AccountEntityTest {

    @Test
    void gettersAndSettersShouldWork() {
        Instant createdAt = Instant.parse("2025-01-01T00:00:00Z");
        Instant updatedAt = Instant.parse("2025-01-02T00:00:00Z");

        AccountEntity entity = new AccountEntity();
        entity.setId(1L);
        entity.setOwnerName("Ivan");
        entity.setBalance(new BigDecimal("1000.00"));
        entity.setCurrency(CurrencyCode.RUB);
        entity.setAccountStatus(AccountStatus.ACTIVE);
        entity.setCreatedAt(createdAt);
        entity.setUpdatedAt(updatedAt);

        assertEquals(1L, entity.getId());
        assertEquals("Ivan", entity.getOwnerName());
        assertEquals(new BigDecimal("1000.00"), entity.getBalance());
        assertEquals(CurrencyCode.RUB, entity.getCurrency());
        assertEquals(AccountStatus.ACTIVE, entity.getAccountStatus());
        assertEquals(createdAt, entity.getCreatedAt());
        assertEquals(updatedAt, entity.getUpdatedAt());
    }
}
