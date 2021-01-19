package com.jindognoob.sermon.service;

import com.jindognoob.sermon.domain.Account;
import com.jindognoob.sermon.domain.etypes.AccountSignupType;


public interface AccountService {
    /** 회원가입 하면서 Point도 생성 */
    public Long signup(String email, String password, AccountSignupType type);
    // public Long signin(Account account); oauth, spring security 적용
    public boolean changepassword(String principal, String currentPassword, String newPassword);
    public Account getAccountInfo(Long id);
    


}
