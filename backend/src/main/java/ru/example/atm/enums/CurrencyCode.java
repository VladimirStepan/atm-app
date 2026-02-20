package ru.example.atm.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum CurrencyCode {
    RUB,
    USD,
    EUR;

    @JsonValue
    public String toJson() {
        return name().toLowerCase();
    }
}
