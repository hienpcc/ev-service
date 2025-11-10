package com.ev.auth.repository;

import com.ev.auth.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    // Phương thức quan trọng để Spring Security tìm user
    Optional<Account> findByUsername(String username);
}
