package ru.example.atm.mapper;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import ru.example.atm.dto.AccountCreateDto;
import ru.example.atm.dto.AccountResponseDto;
import ru.example.atm.dto.AccountUpdateDto;
import ru.example.atm.entity.AccountEntity;
import ru.example.atm.enums.AccountStatus;
import ru.example.atm.enums.CurrencyCode;

import java.math.BigDecimal;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class AccountMapperTest {

    private final AccountMapper mapper = Mappers.getMapper(AccountMapper.class);

    @Test
    void toEntityShouldMapCreateDtoAndIgnoreManagedFields() {
        AccountCreateDto dto = new AccountCreateDto("Ivan", new BigDecimal("10.50"), CurrencyCode.RUB);

        AccountEntity entity = mapper.toEntity(dto);

        assertNotNull(entity);
        assertEquals("Ivan", entity.getOwnerName());
        assertEquals(new BigDecimal("10.50"), entity.getBalance());
        assertEquals(CurrencyCode.RUB, entity.getCurrency());
        assertNull(entity.getId());
        assertNull(entity.getAccountStatus());
        assertNull(entity.getCreatedAt());
        assertNull(entity.getUpdatedAt());
    }

    @Test
    void toEntityShouldReturnNullWhenSourceIsNull() {
        assertNull(mapper.toEntity(null));
    }

    @Test
    void toResponseShouldMapAllFields() {
        Instant createdAt = Instant.parse("2025-01-01T00:00:00Z");
        Instant updatedAt = Instant.parse("2025-01-02T00:00:00Z");

        AccountEntity entity = new AccountEntity();
        entity.setId(11L);
        entity.setOwnerName("Petr");
        entity.setBalance(new BigDecimal("42.00"));
        entity.setCurrency(CurrencyCode.USD);
        entity.setAccountStatus(AccountStatus.FROZEN);
        entity.setCreatedAt(createdAt);
        entity.setUpdatedAt(updatedAt);

        AccountResponseDto dto = mapper.toResponse(entity);

        assertNotNull(dto);
        assertEquals(11L, dto.id());
        assertEquals("Petr", dto.ownerName());
        assertEquals(new BigDecimal("42.00"), dto.balance());
        assertEquals(CurrencyCode.USD, dto.currency());
        assertEquals(AccountStatus.FROZEN, dto.accountStatus());
        assertEquals(createdAt, dto.createdAt());
        assertEquals(updatedAt, dto.updatedAt());
    }

    @Test
    void toResponseShouldReturnNullWhenSourceIsNull() {
        assertNull(mapper.toResponse(null));
    }

    @Test
    void updateEntityShouldIgnoreNullsAndKeepIdAndTimestamps() {
        Instant createdAt = Instant.parse("2025-01-01T00:00:00Z");
        Instant updatedAt = Instant.parse("2025-01-02T00:00:00Z");

        AccountEntity entity = new AccountEntity();
        entity.setId(1L);
        entity.setOwnerName("Old Name");
        entity.setBalance(new BigDecimal("100.00"));
        entity.setCurrency(CurrencyCode.RUB);
        entity.setAccountStatus(AccountStatus.ACTIVE);
        entity.setCreatedAt(createdAt);
        entity.setUpdatedAt(updatedAt);

        AccountUpdateDto dto = new AccountUpdateDto(null, new BigDecimal("150.00"), null, AccountStatus.CLOSED);
        mapper.updateEntity(dto, entity);

        assertEquals(1L, entity.getId());
        assertEquals("Old Name", entity.getOwnerName());
        assertEquals(new BigDecimal("150.00"), entity.getBalance());
        assertEquals(CurrencyCode.RUB, entity.getCurrency());
        assertEquals(AccountStatus.CLOSED, entity.getAccountStatus());
        assertEquals(createdAt, entity.getCreatedAt());
        assertEquals(updatedAt, entity.getUpdatedAt());
    }

    @Test
    void updateEntityShouldDoNothingWhenDtoIsNull() {
        AccountEntity entity = new AccountEntity();
        entity.setOwnerName("Name");

        mapper.updateEntity(null, entity);

        assertEquals("Name", entity.getOwnerName());
    }
}
