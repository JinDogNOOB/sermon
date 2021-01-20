package com.jindognoob.sermon.service;

import com.jindognoob.sermon.domain.etypes.AccountSignupType;
import com.jindognoob.sermon.dto.AccountDTO;


public interface AccountService {
    /** 회원가입 하면서 Point도 생성 */
    public Long signup(String email, String password, String nickname, AccountSignupType type);
    // public Long signin(Account account); oauth, spring security 적용
    public boolean changepassword(String principal, String currentPassword, String newPassword);
    public AccountDTO getAccountInfo(Long id);

    public boolean simpleAuthentication(String email, String password);

    // 회원탈퇴
    public void quit(String email, String password);
    


}
