package com.jindognoob.sermon.config.security.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jindognoob.sermon.config.security.AccountUserDetail;
import com.jindognoob.sermon.config.security.constants.AuthConstants;
import com.jindognoob.sermon.config.security.utils.TokenUtils;
import com.jindognoob.sermon.domain.Account;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

public class CustomLoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler{
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        // TODO Auto-generated method stub
        // SecurityContextHolder.getContext().setAuthentication(authentication);
        Account account = ((AccountUserDetail)authentication.getPrincipal()).getAccount();

        String token = TokenUtils.generateJwtToken(account);

        response.addHeader(AuthConstants.AUTH_HEADER, AuthConstants.TOKEN_TYPE + " " + token);
        // response.sendRedirect("/welcome");
    }
}
