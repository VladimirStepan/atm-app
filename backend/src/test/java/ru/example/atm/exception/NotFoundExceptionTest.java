package ru.example.atm.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class NotFoundExceptionTest {

    @Test
    void shouldStoreProvidedMessage() {
        NotFoundException ex = new NotFoundException("Account not found");
        assertEquals("Account not found", ex.getMessage());
    }
}
