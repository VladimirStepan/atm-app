package ru.example.atm.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import ru.example.atm.enums.AccountStatus;
import ru.example.atm.enums.CurrencyCode;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(
        name = "accounts",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_accounts_owner_name", columnNames = "owner_name")
        }
)
public class AccountEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "owner_name", nullable = false)
    private String ownerName;

    @Column(nullable = false)
    private BigDecimal balance;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CurrencyCode currency;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountStatus accountStatus;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private Instant updatedAt;
}
