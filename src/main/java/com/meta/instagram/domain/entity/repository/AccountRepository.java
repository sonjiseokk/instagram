package com.meta.instagram.domain.entity.repository;

import com.meta.instagram.domain.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
}
