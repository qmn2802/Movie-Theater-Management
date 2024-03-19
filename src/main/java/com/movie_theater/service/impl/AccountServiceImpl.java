package com.movie_theater.service.impl;

import com.movie_theater.entity.Account;
import com.movie_theater.entity.Role;
import com.movie_theater.final_attribute.ROLE;
import com.movie_theater.repository.AccountRepository;
import com.movie_theater.repository.RoleRepository;
import com.movie_theater.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static com.movie_theater.service.fogotPassWord.javamail.SendHtmlEmail.generateRandomPassword;
import static com.movie_theater.service.fogotPassWord.javamail.SendHtmlEmail.sendNewPassWordToEmail;

@Service
public class AccountServiceImpl implements AccountService {
    AccountRepository accountRepository;
    RoleRepository roleRepository;

    @Autowired
    public AccountServiceImpl(AccountRepository accountRepository, RoleRepository roleRepository) {
        this.accountRepository = accountRepository;
        this.roleRepository = roleRepository;
    }

    @Transactional
    @Override
    public Account save(Account account) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        account.setPassword(bCryptPasswordEncoder.encode(account.getPassword()));
        account.setRegisterDate(LocalDate.now());
        account.setDeleted(false);
        account.setScore(0);
        return accountRepository.save(account);
    }

    public void updateNewPassWord(String email) {
        String newPass = generateRandomPassword();
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String newPassBcrypt = bCryptPasswordEncoder.encode(newPass);
        accountRepository.updateAccountByEmail(newPassBcrypt, email);
        sendNewPassWordToEmail(newPass, email);
    }

    @Override
    @Transactional
    public Account getByUsername(String userName) {
        return accountRepository.getByUsernameAndDeletedIsFalse(userName);
    }



    @Transactional
    @Override
    public void updateAccount(Account account) {
        accountRepository.save(account);
    }

    @Transactional
    @Override
    public void updateAccountImage(String username, String newImage) {
        accountRepository.updateAccountImage(username, newImage);
    }
    @Transactional
    @Override
    public int updateAccountByAccountUserName(String username, String newPass,String oldPassword) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

        Account account = accountRepository.getByUsernameAndDeletedIsFalse(username);

        if (!bCryptPasswordEncoder.matches(oldPassword,account.getPassword())){
            return 0;
        }
        String newPassBcrypt = bCryptPasswordEncoder.encode(newPass);
        return accountRepository.updateAccountByAccountUserName(username,newPassBcrypt);
    }

    @Override
    public Page<Account> getAllEmployee(String fullName, Pageable pageable) {
        Role role = roleRepository.getByRoleName(ROLE.EMPLOYEE);
        return accountRepository.getByFullNameContainsAndRoleAndDeletedIsFalse(fullName,role,pageable);
    }

    @Override
    public Account getAccountByAccountId(Integer accountId) {
        return accountRepository.getAccountByAccountId(accountId);
    }

    @Override
    public void deleteEmployee(Account account) {
        accountRepository.deActiveAccount(account);
    }
}