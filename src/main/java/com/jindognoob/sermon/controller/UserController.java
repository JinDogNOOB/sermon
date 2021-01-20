package com.jindognoob.sermon.controller;


import com.jindognoob.sermon.domain.etypes.AccountSignupType;
import com.jindognoob.sermon.service.AccountService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    AccountService accountService;

    /** 회원 가입 */
    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public void signup(@RequestParam("email") String email, @RequestParam("password") String password,
            @RequestParam("nickname") String nickname) {
        accountService.signup(email, password, nickname, AccountSignupType.THIS);
        return;
    }

    /** 토큰 유효성 확인 */
    @RequestMapping(value = "/auth", method = RequestMethod.GET)
    public void validateToken() {
        return;
    }

    /** 회원 정보(패스워드) 수정 */
    @RequestMapping(value = "/auth", method = RequestMethod.PUT)
    public void modifyUserInfo(@RequestParam("currentPassword") String currentPassword,
            @RequestParam("newPassword") String newPassword) {
        String principal = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        accountService.changepassword(principal, currentPassword, newPassword);
        return;
    }

    /** 회원 탈퇴 */
    @RequestMapping(value = "/auth", method = RequestMethod.DELETE)
    public void deleteUser(@RequestParam("password") String password) {
        String principal = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        accountService.quit(principal, password);
        return;
    }

}
