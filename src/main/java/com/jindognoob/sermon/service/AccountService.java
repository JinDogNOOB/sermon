package com.jindognoob.sermon.service;

import com.jindognoob.sermon.domain.Account;
import com.jindognoob.sermon.domain.etypes.AccountSignupType;
import com.jindognoob.sermon.repository.AccountRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


public interface AccountService {
    
    public Long signup(String email, String password, AccountSignupType type);
    // public Long signin(Account account); oauth, spring security 적용

    public Account getAccountInfo(Long id);
    


}
