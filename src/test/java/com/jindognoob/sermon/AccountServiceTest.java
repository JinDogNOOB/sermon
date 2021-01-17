package com.jindognoob.sermon;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import com.jindognoob.sermon.domain.Account;
import com.jindognoob.sermon.domain.etypes.AccountSignupType;
import com.jindognoob.sermon.service.AccountService;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class AccountServiceTest {
    @Autowired AccountService accountService;
    
    @Test
    public void 유저_회원가입(){
        Long id = accountService.signup("test1@test.com", "qwer1234", AccountSignupType.THIS);
        Account account = accountService.getAccountInfo(id);

        assertEquals("test1@test.com", account.getEmail());
    }

    @Test
    public void 유저_중복가입_예외(){
        accountService.signup("test1@test.com", "qwer1234", AccountSignupType.THIS);
        
        

        Assertions.assertThrows(IllegalStateException.class, () -> {
            accountService.signup("test1@test.com", "qwer1234", AccountSignupType.THIS);
        });
        // fail("유저 중복가입 예외 발생 안함");
    }






    
    private Account createTestAccount(){
        Long id = accountService.signup("test1@test.com", "qwer1234", AccountSignupType.THIS);
        return accountService.getAccountInfo(id);
    }

    
}
