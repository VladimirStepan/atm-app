package ru.example.atm.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.example.atm.entity.AccountEntity;

@Repository
public interface AccountRepository extends JpaRepository<AccountEntity, Long> {
    boolean existsByOwnerNameIgnoreCase(String ownerName);

    boolean existsByOwnerNameIgnoreCaseAndIdNot(String ownerName, Long id);
}
