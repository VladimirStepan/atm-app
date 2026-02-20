package ru.example.atm.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import ru.example.atm.enums.CurrencyCode;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

public record AccountCreateDto(
        @NotBlank
        String ownerName,
        @NotNull
        @PositiveOrZero
        BigDecimal balance,
        @NotNull
        CurrencyCode currency)
{

}
