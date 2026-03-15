package ru.example.atm.controller.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.example.atm.controller.exception.GlobalExceptionHandler;
import ru.example.atm.dto.AccountResponseDto;
import ru.example.atm.enums.AccountStatus;
import ru.example.atm.enums.CurrencyCode;
import ru.example.atm.exception.AlreadyExistsException;
import ru.example.atm.exception.NotFoundException;
import ru.example.atm.service.AccountService;

import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.Matchers.endsWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class AccountControllerTest {

    @Mock
    private AccountService accountService;

    @InjectMocks
    private AccountController accountController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(accountController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void createAccountShouldReturn201AndBody() throws Exception {
        AccountResponseDto response = new AccountResponseDto(
                1L, "Ivan", new BigDecimal("1200.00"), CurrencyCode.RUB, AccountStatus.ACTIVE, null, null
        );
        when(accountService.createAccount(any())).thenReturn(response);

        mockMvc.perform(post("/api/v1/atm")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "ownerName":"Ivan",
                                  "balance":1200.00,
                                  "currency":"rub"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", endsWith("/api/v1/atm/1")))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.ownerName").value("Ivan"))
                .andExpect(jsonPath("$.currency").value("rub"))
                .andExpect(jsonPath("$.accountStatus").value("active"));
    }

    @Test
    void getByIdShouldReturn200AndBody() throws Exception {
        AccountResponseDto response = new AccountResponseDto(
                2L, "Petr", new BigDecimal("300.00"), CurrencyCode.USD, AccountStatus.FROZEN, null, null
        );
        when(accountService.getById(2L)).thenReturn(response);

        mockMvc.perform(get("/api/v1/atm/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.ownerName").value("Petr"))
                .andExpect(jsonPath("$.currency").value("usd"))
                .andExpect(jsonPath("$.accountStatus").value("frozen"));
    }

    @Test
    void getAllShouldReturn200AndArray() throws Exception {
        AccountResponseDto first = new AccountResponseDto(1L, "A", BigDecimal.ONE, CurrencyCode.EUR, AccountStatus.ACTIVE, null, null);
        AccountResponseDto second = new AccountResponseDto(2L, "B", BigDecimal.TEN, CurrencyCode.USD, AccountStatus.CLOSED, null, null);
        when(accountService.getAll()).thenReturn(List.of(first, second));

        mockMvc.perform(get("/api/v1/atm"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2));
    }

    @Test
    void updateShouldReturn200AndUpdatedBody() throws Exception {
        AccountResponseDto response = new AccountResponseDto(
                7L, "Updated", new BigDecimal("999.99"), CurrencyCode.USD, AccountStatus.FROZEN, null, null
        );
        when(accountService.update(any(), any())).thenReturn(response);

        mockMvc.perform(put("/api/v1/atm/7")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "ownerName":"Updated",
                                  "balance":999.99,
                                  "currency":"usd",
                                  "accountStatus":"frozen"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(7))
                .andExpect(jsonPath("$.ownerName").value("Updated"));
    }

    @Test
    void deleteShouldReturn204() throws Exception {
        mockMvc.perform(delete("/api/v1/atm/3"))
                .andExpect(status().isNoContent());

        verify(accountService).delete(3L);
    }

    @Test
    void shouldReturn404WhenServiceThrowsNotFound() throws Exception {
        when(accountService.getById(404L)).thenThrow(new NotFoundException("Account not found: id=404"));

        mockMvc.perform(get("/api/v1/atm/404"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Account not found: id=404"));
    }

    @Test
    void shouldReturn409WhenServiceThrowsAlreadyExists() throws Exception {
        when(accountService.createAccount(any())).thenThrow(new AlreadyExistsException("Account already exists: ownerName=Ivan"));

        mockMvc.perform(post("/api/v1/atm")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "ownerName":"Ivan",
                                  "balance":1200.00,
                                  "currency":"rub"
                                }
                                """))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("Account already exists: ownerName=Ivan"));
    }

    @Test
    void deleteShouldReturn404WhenEntityMissing() throws Exception {
        doThrow(new NotFoundException("Account not found: id=99")).when(accountService).delete(99L);

        mockMvc.perform(delete("/api/v1/atm/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Account not found: id=99"));
    }
}
