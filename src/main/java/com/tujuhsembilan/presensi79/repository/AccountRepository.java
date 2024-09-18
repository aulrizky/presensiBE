package com.tujuhsembilan.presensi79.repository;

import com.tujuhsembilan.presensi79.model.Account;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, String> {
    boolean existsByUsername(String username);
    Optional<Account> findByUsername(String username);
}
