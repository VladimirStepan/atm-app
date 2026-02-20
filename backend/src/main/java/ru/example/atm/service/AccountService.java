package ru.example.atm.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import ru.example.atm.dto.AccountCreateDto;
import ru.example.atm.dto.AccountResponseDto;
import ru.example.atm.dto.AccountUpdateDto;
import ru.example.atm.entity.AccountEntity;
import ru.example.atm.enums.AccountStatus;
import ru.example.atm.exception.NotFoundException;
import ru.example.atm.mapper.AccountMapper;
import ru.example.atm.repo.AccountRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Validated
public class AccountService {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;

    private static final String ACCOUNT_NOT_FOUND_MESSAGE = "Account not found: id=";

    @Transactional
    public AccountResponseDto createAccount(@Valid AccountCreateDto accountCreateDto) {
        AccountEntity entity = accountMapper.toEntity(accountCreateDto);
        entity.setAccountStatus(AccountStatus.ACTIVE);

        AccountEntity saved = accountRepository.save(entity);
        return accountMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public AccountResponseDto getById(Long id) {
        AccountEntity entity = accountRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ACCOUNT_NOT_FOUND_MESSAGE + id));
        return accountMapper.toResponse(entity);
    }


    @Transactional(readOnly = true)
    public List<AccountResponseDto> getAll() {
        return accountRepository.findAll().stream().map(accountMapper::toResponse).toList();
    }

    @Transactional
    public AccountResponseDto update(Long id, AccountUpdateDto request) {
        AccountEntity entity = accountRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ACCOUNT_NOT_FOUND_MESSAGE + id));

        accountMapper.updateEntity(request, entity);

        AccountEntity saved = accountRepository.save(entity);
        return accountMapper.toResponse(saved);
    }

    @Transactional
    public void delete(Long id) {
        if (!accountRepository.existsById(id)) {
            throw new NotFoundException(ACCOUNT_NOT_FOUND_MESSAGE + id);
        }
        accountRepository.deleteById(id);
    }
}
