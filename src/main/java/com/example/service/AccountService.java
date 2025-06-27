package com.example.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.entity.Account;
import com.example.repository.AccountRepository;
import com.example.exception.*;
import java.util.Optional;

@Service
public class AccountService {
    private final AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    /**
     * Registers a new account if the username is not already taken and the credentials are valid.
     *
     * @param account The account object containing username and password to be registered.
     * @return The new Account entity.
     * @throws UsernameAlreadyExistsException if the username already exists in the repository.
     * @throws IllegalArgumentException if the username is empty or the password is too short.
     */
    public Account registerAccount(Account account)
        throws UsernameAlreadyExistsException, IllegalArgumentException{
        String username = account.getUsername();
        String password = account.getPassword();

        if (username.length() <= 0 || password.length() < 4) {
            throw new IllegalArgumentException("Invalid account details.");
        } else if(accountRepository.findByUsername(username).isPresent()) {
            throw new UsernameAlreadyExistsException("Username already exists.");
        }

        return accountRepository.save(new Account(username, password));
    }

    /**
     * Tries to log in to an account by verifying the provided credentials.
     *
     * @param account The account object containing username and password.
     * @return The matching Account if credentials are valid; otherwise, returns null.
     */
    public Account loginToAccount(Account account) {
        String username = account.getUsername();
        String password = account.getPassword();

        Optional<Account> optional = accountRepository.findByUsername(username);
        if (optional.isPresent()) {
            Account existing = optional.get();
            if (existing.getPassword().equals(password)) {
                return existing;
            }
        }

        return null; 
    }
}
