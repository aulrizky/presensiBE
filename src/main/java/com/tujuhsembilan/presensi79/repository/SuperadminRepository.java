package com.tujuhsembilan.presensi79.repository;

import com.tujuhsembilan.presensi79.model.Account;
import com.tujuhsembilan.presensi79.model.Superadmin;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SuperadminRepository extends JpaRepository<Superadmin, Integer> {
    Optional<Superadmin> findByAccount(Account account);
}
