package com.jindognoob.sermon.service;

import java.util.List;

import com.jindognoob.sermon.domain.Account;
import com.jindognoob.sermon.domain.etypes.AccountSignupType;
import com.jindognoob.sermon.repository.AccountRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AccountServiceImpl implements AccountService {

    @Autowired AccountRepository accountRepository;

    @Autowired PasswordEncoder passwordEncoder;

    @Override
    public Account getAccountInfo(Long id) {
        return accountRepository.findOne(id);
    }

    @Override
    public Long signup(String email, String password, AccountSignupType type) {
        Account account = new Account();
        account.setEmail(email);
        account.setPassword(passwordEncoder.encode(password));
        account.setSignupType(type);
        
        validateDuplicateAccount(account);
        accountRepository.save(account);
        return account.getId();
    }







    private void validateDuplicateAccount(Account account){
        List<Account> accounts = accountRepository.findByEmail(account.getEmail());
        if(!accounts.isEmpty()) throw new IllegalStateException("이미 존재하는 EMAIL(회원)입니다.");
    }
    
}
