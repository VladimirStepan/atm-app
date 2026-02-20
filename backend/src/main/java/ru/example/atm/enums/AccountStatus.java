package ru.example.atm.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum AccountStatus {
    ACTIVE,
    FROZEN,
    CLOSED;

    @JsonValue
    public String toJson() {
        return name().toLowerCase();
    }
}
