package com.jindognoob.sermon.service;

import java.util.List;

import com.jindognoob.sermon.domain.Account;
import com.jindognoob.sermon.domain.Point;
import com.jindognoob.sermon.domain.etypes.AccountRoleType;
import com.jindognoob.sermon.domain.etypes.AccountSignupType;
import com.jindognoob.sermon.dto.AccountDTO;
import com.jindognoob.sermon.repository.AccountRepository;
import com.jindognoob.sermon.repository.PointRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AccountServiceJPAImpl implements AccountService {

    @Autowired AccountRepository accountRepository;
    @Autowired PointRepository pointRepository;
    @Autowired PasswordEncoder passwordEncoder;



    @Override
    public Long signup(String email, String password, String nickname, AccountSignupType type) {
        Account account = new Account();
        account.setEmail(email);
        account.setPassword(passwordEncoder.encode(password));
        account.setSignupType(type);
        account.setNickname(nickname);
        account.setRoleType(AccountRoleType.ROLE_USER);
        
        validateDuplicateAccount(account);

        accountRepository.save(account);

        Point point = new Point();
        point.setAmount((long)0);
        point.setAccount(account);
        pointRepository.save(point);

        return account.getId();
    }

    @Override
    public AccountDTO getAccountInfo(Long id) {
        return AccountDTO.of(accountRepository.findOne(id));
    }

    @Override
    public boolean changepassword(String principal, String currentPassword, String newPassword){
        Account account = accountRepository.findOneByEmail(principal);
        if(account==null) return false;
        if(passwordEncoder.matches(currentPassword, account.getPassword())){
            account.setPassword(passwordEncoder.encode(newPassword));
            return true;
        }
        return false;
    }


    @Override
    public boolean simpleAuthentication(String email, String password){
        return passwordEncoder.matches(password, accountRepository.findOneByEmail(email).getPassword());
    }

    @Override
    public void quit(String email, String password){
        Account account = accountRepository.findOneByEmail(email);
        account.setEmail("X");
        account.setPassword("X");
        account.setRoleType(AccountRoleType.ROLE_BANNED);
        account.setSignupType(AccountSignupType.THIS);
    }







    private void validateDuplicateAccount(Account account){
        List<Account> accounts = accountRepository.findByEmail(account.getEmail());
        if(!accounts.isEmpty()) throw new IllegalStateException("이미 존재하는 EMAIL(회원)입니다.");
    }
    
}
