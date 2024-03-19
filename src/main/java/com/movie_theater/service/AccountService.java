package com.movie_theater.service;


import com.movie_theater.entity.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AccountService {
    Account save(Account account);

    void updateNewPassWord(String email);
    Account getByUsername (String userName);


    void updateAccount(Account account);

    void updateAccountImage(String username,String newImage);

    int updateAccountByAccountUserName(String username,String newPass,String oldPassword);

    Page<Account> getAllEmployee(String fullName, Pageable pageable);

    Account getAccountByAccountId(Integer accountId);

    void deleteEmployee(Account account);
}