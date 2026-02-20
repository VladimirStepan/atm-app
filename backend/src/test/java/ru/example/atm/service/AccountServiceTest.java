package ru.example.atm.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.example.atm.dto.AccountCreateDto;
import ru.example.atm.dto.AccountResponseDto;
import ru.example.atm.dto.AccountUpdateDto;
import ru.example.atm.entity.AccountEntity;
import ru.example.atm.enums.AccountStatus;
import ru.example.atm.enums.CurrencyCode;
import ru.example.atm.exception.NotFoundException;
import ru.example.atm.mapper.AccountMapper;
import ru.example.atm.repo.AccountRepository;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private AccountMapper accountMapper;

    @InjectMocks
    private AccountService accountService;

    @Test
    void createAccountShouldSetActiveStatusAndReturnResponse() {
        AccountCreateDto request = new AccountCreateDto("Ivan", new BigDecimal("100.00"), CurrencyCode.RUB);
        AccountEntity toSave = new AccountEntity();
        AccountEntity saved = new AccountEntity();
        saved.setId(1L);
        AccountResponseDto expected = new AccountResponseDto(
                1L, "Ivan", new BigDecimal("100.00"), CurrencyCode.RUB, AccountStatus.ACTIVE, Instant.now(), Instant.now()
        );

        when(accountMapper.toEntity(request)).thenReturn(toSave);
        when(accountRepository.save(toSave)).thenReturn(saved);
        when(accountMapper.toResponse(saved)).thenReturn(expected);

        AccountResponseDto actual = accountService.createAccount(request);

        assertSame(expected, actual);
        assertEquals(AccountStatus.ACTIVE, toSave.getAccountStatus());
        verify(accountRepository).save(toSave);
        verify(accountMapper).toResponse(saved);
    }

    @Test
    void getByIdShouldReturnResponseWhenEntityExists() {
        AccountEntity entity = new AccountEntity();
        entity.setId(10L);
        AccountResponseDto expected = new AccountResponseDto(
                10L, "Petr", new BigDecimal("250.00"), CurrencyCode.USD, AccountStatus.ACTIVE, null, null
        );

        when(accountRepository.findById(10L)).thenReturn(Optional.of(entity));
        when(accountMapper.toResponse(entity)).thenReturn(expected);

        AccountResponseDto actual = accountService.getById(10L);

        assertSame(expected, actual);
        verify(accountRepository).findById(10L);
        verify(accountMapper).toResponse(entity);
    }

    @Test
    void getByIdShouldThrowWhenEntityMissing() {
        when(accountRepository.findById(99L)).thenReturn(Optional.empty());

        NotFoundException ex = assertThrows(NotFoundException.class, () -> accountService.getById(99L));

        assertEquals("Account not found: id=99", ex.getMessage());
        verify(accountRepository).findById(99L);
        verifyNoInteractions(accountMapper);
    }

    @Test
    void getAllShouldMapAllEntities() {
        AccountEntity first = new AccountEntity();
        AccountEntity second = new AccountEntity();

        AccountResponseDto firstDto = new AccountResponseDto(1L, "A", BigDecimal.ONE, CurrencyCode.EUR, AccountStatus.ACTIVE, null, null);
        AccountResponseDto secondDto = new AccountResponseDto(2L, "B", BigDecimal.TEN, CurrencyCode.USD, AccountStatus.FROZEN, null, null);

        when(accountRepository.findAll()).thenReturn(List.of(first, second));
        when(accountMapper.toResponse(first)).thenReturn(firstDto);
        when(accountMapper.toResponse(second)).thenReturn(secondDto);

        List<AccountResponseDto> actual = accountService.getAll();

        assertEquals(List.of(firstDto, secondDto), actual);
        verify(accountRepository).findAll();
    }

    @Test
    void updateShouldApplyMapperAndReturnUpdatedResponse() {
        AccountUpdateDto request = new AccountUpdateDto("New Name", new BigDecimal("500.00"), CurrencyCode.USD, AccountStatus.FROZEN);
        AccountEntity existing = new AccountEntity();
        existing.setId(7L);
        AccountResponseDto expected = new AccountResponseDto(
                7L, "New Name", new BigDecimal("500.00"), CurrencyCode.USD, AccountStatus.FROZEN, null, null
        );

        when(accountRepository.findById(7L)).thenReturn(Optional.of(existing));
        when(accountRepository.save(existing)).thenReturn(existing);
        when(accountMapper.toResponse(existing)).thenReturn(expected);

        AccountResponseDto actual = accountService.update(7L, request);

        assertSame(expected, actual);
        verify(accountMapper).updateEntity(request, existing);
        verify(accountRepository).save(existing);
    }

    @Test
    void updateShouldThrowWhenEntityMissing() {
        AccountUpdateDto request = new AccountUpdateDto("Name", BigDecimal.ZERO, CurrencyCode.RUB, AccountStatus.CLOSED);
        when(accountRepository.findById(42L)).thenReturn(Optional.empty());

        NotFoundException ex = assertThrows(NotFoundException.class, () -> accountService.update(42L, request));

        assertEquals("Account not found: id=42", ex.getMessage());
        verify(accountRepository).findById(42L);
    }

    @Test
    void deleteShouldCallRepositoryWhenEntityExists() {
        when(accountRepository.existsById(5L)).thenReturn(true);

        accountService.delete(5L);

        verify(accountRepository).deleteById(5L);
    }

    @Test
    void deleteShouldThrowWhenEntityMissing() {
        when(accountRepository.existsById(5L)).thenReturn(false);

        NotFoundException ex = assertThrows(NotFoundException.class, () -> accountService.delete(5L));

        assertEquals("Account not found: id=5", ex.getMessage());
    }
}
