package com.jindognoob.sermon.config.security;

import com.jindognoob.sermon.config.security.filter.CustomAuthenticationFilter;
import com.jindognoob.sermon.config.security.filter.JWTAuthorizationFilter;
import com.jindognoob.sermon.config.security.handler.CustomLoginSuccessHandler;
import com.jindognoob.sermon.config.security.provider.CustomAuthenticationProvider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    CustomAuthenticationProvider authProvider;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authProvider);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        /**
         * 토큰을 활용하는 경우 모든 요청에 대해 접근 가능하게하고 세션이 필요하지않으므로 비활성화 form 기반의 로그인에 대해 비활성화 한다 아
         * 일단 활성화해서 로그인성공까지해서 토큰 클라이언트에 보내긴했는데 그다음부터 클라이언트가 이 토큰을 가지고있다가 요청시마다 보내야하는데
         * 기존의 클라이언트html에서는 하나하나보내야하나,,
         */
        http.csrf().disable()
        .authorizeRequests()
        .antMatchers("/admin/**").hasRole("ADMIN")
        .antMatchers("/auth/**", "/console/**").permitAll()
        .anyRequest().authenticated().and()
        .formLogin().disable()
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                /*
                 * .logout() .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET"))
                 * //.logoutUrl("/logout") .permitAll() .and()
                 */
                .addFilterBefore(customAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilter(jwtAuthorizationFilter());

    }

    // 패스워드 인코더
    @Bean
    public PasswordEncoder passwordEncoder() {
        // return new BCryptPasswordEncoder();
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    // CustomAuthenticationFilter Bean 생성
    @Bean
    public CustomAuthenticationFilter customAuthenticationFilter() throws Exception {
        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(authenticationManager());
        customAuthenticationFilter.setFilterProcessesUrl("/user/login");
        customAuthenticationFilter.setAuthenticationSuccessHandler(customLoginSuccessHandler());
        customAuthenticationFilter.afterPropertiesSet();
        return customAuthenticationFilter;
    }

    // JWTAuthorizationFilter Bean
    @Bean
    public JWTAuthorizationFilter jwtAuthorizationFilter() throws Exception {
        return new JWTAuthorizationFilter(authenticationManager());
    }

    @Bean
    public CustomLoginSuccessHandler customLoginSuccessHandler() {
        return new CustomLoginSuccessHandler();
    }

}
