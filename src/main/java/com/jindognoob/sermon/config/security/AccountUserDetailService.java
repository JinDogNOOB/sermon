package com.jindognoob.sermon.config.security;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

import com.jindognoob.sermon.domain.Account;
import com.jindognoob.sermon.domain.etypes.AccountRoleType;
import com.jindognoob.sermon.repository.AccountRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AccountUserDetailService implements UserDetailsService{
    @Autowired private AccountRepository accountRepository;

        // loadUserByUsername 반드시 구현
        @Transactional
        @Override
        public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
            // TODO Auto-generated method stub
            Optional<Account> byUserEmail = accountRepository.findOneByEmailAsOptional(username);
            Account account = byUserEmail.orElseThrow(() -> new UsernameNotFoundException(username));
            return new AccountUserDetail(account.getEmail(), account.getPassword(), authorities(account.getRoleType()));
        }
        
        private Collection<? extends GrantedAuthority> authorities(AccountRoleType role){
            return Arrays.asList(new SimpleGrantedAuthority(role.toString()));
        }
    
    
}
