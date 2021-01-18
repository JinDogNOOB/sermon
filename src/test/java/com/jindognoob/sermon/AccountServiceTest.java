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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class AccountServiceTest {
    @Autowired AccountService accountService;
    @Autowired PasswordEncoder passwordEncoder;
    
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

    @Test
    public void 유저_패스워드변경(){
        String cp = "qwer1234";
        String np = "aaaabbbb";
        Long id = accountService.signup("test@test.com", cp, AccountSignupType.THIS);
        accountService.changepassword("test@test.com", cp, np);
    
        assertEquals(true, passwordEncoder.matches(np, accountService.getAccountInfo(id).getPassword()));
    }

    @Test
    public void 유저_회원가입시_포인트레코드생성_확인(){
        Account account = createTestAccount();

        assertEquals(0, account.getPoint().getAmount());
    }

    @Test
    public void 유저_포인트_증가_확인(){
        Account account = createTestAccount();

        account.getPoint().addPoint((long)30);

        assertEquals(30, account.getPoint().getAmount());
    }

    @Test
    public void 유저_포인트_마이너스_예외_확인(){
        Account account = createTestAccount();

        account.getPoint().addPoint((long)30);

        Assertions.assertThrows(Exception.class, () -> {
            account.getPoint().subPoint((long)31);
        });
    }









    
    private Account createTestAccount(){
        Long id = accountService.signup("tester@test.com", "qwer1234", AccountSignupType.THIS);
        return accountService.getAccountInfo(id);
    }

    
}
