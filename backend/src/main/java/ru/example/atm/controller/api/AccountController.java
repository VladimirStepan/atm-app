package ru.example.atm.controller.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.example.atm.dto.AccountCreateDto;
import ru.example.atm.dto.AccountResponseDto;
import ru.example.atm.dto.AccountUpdateDto;
import ru.example.atm.service.AccountService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/atm")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    public ResponseEntity<?> createAccount(@RequestBody AccountCreateDto accountCreateDto) {
        AccountResponseDto accountResponseDto = accountService.createAccount(accountCreateDto);
        return ResponseEntity.created(URI.create("/api/accounts/" + accountResponseDto.id()))
                .body(accountResponseDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountResponseDto> get(@PathVariable Long id) {
        return ResponseEntity.ok(accountService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<AccountResponseDto>> getAll() {
        return ResponseEntity.ok(accountService.getAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<AccountResponseDto> update(
            @PathVariable Long id,
            @RequestBody AccountUpdateDto request
    ) {
        return ResponseEntity.ok(accountService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        accountService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
