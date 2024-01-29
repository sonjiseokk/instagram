package com.meta.instagram.service.security;

import com.meta.instagram.domain.dto.security.CustomAccountDetails;
import com.meta.instagram.domain.entity.Account;
import com.meta.instagram.domain.entity.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomAccountDetailsService implements UserDetailsService {
    private final AccountRepository accountRepository;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Account> findAccount = accountRepository.findByEmail(email);

        if (findAccount.isPresent()) {
            Account account = findAccount.orElseThrow();
            return new CustomAccountDetails(account);
        }
        return null;
    }
}
