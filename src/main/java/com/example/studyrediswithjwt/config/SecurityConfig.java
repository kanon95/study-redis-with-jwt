package com.example.studyrediswithjwt.config;

import com.example.studyrediswithjwt.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;

    @Autowired
    public SecurityConfig(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    // Spring 보안 필터 체인 등록/설정
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // CSRF (Cross-Site Request Forgery, 사이트 간 요청 위조) check disable. ( REST API는 jwt token인증등을 통해, 보안처리 하기 때문에 )
            .csrf(csrf -> csrf.disable())
            // 인증에 따른 uri 접근 허용 설정
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/users/**").authenticated() // 인증된 경우만 접근 허용
                .requestMatchers("/h2-console/**").permitAll() // H2 콘솔 접근 허용
                .requestMatchers("/css/**", "/js/**", "/images/**").permitAll()
                .requestMatchers("/login", "/register", "/error").permitAll()
                .requestMatchers("/test-password").permitAll()
                .anyRequest().permitAll()
            )
            // HTTP Basic 인증을 허용 - 개발시, postman으로 요청시, 필요. - postman header에 Authorization: Basic aG9uZzpwYXNzd29yZDEyMw==  추가
            .httpBasic(Customizer.withDefaults())
            // 로그인 form page url, 처리 url, 성공/실패 url, id/pwd 파라미터 설정.
            .formLogin(formLogin -> formLogin
                .loginPage("/login") // 사용자 정의 로그인 페이지
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/", true)
                .failureUrl("/login?error=true")
                .usernameParameter("username") // 로그인 폼의 사용자 이름 필드 이름
                .passwordParameter("password") // 로그인 폼의 비밀번호 필드 이름
                .permitAll()
            )
            // 로그아웃 처리 설정
            .logout(logout -> logout
                // 로그아웃 요청 url 설정
                .logoutUrl("/logout")
                // 로그아웃 성공 시, 이동 url
                .logoutSuccessUrl("/login?logout=true")
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
            )
            // 세션 설정
            .sessionManagement(session -> session
                // 한 계정당 동시로그인 1개로 제한
                .maximumSessions(1)
                // 세션만료시, 이동할 url 설정
                .expiredUrl("/login?expired=true")
            )
            // 헤더 설정
            .headers(headers -> headers
                // X-Frame-Options: SAMEORIGIN 헤더 추가. 같은 출처(스킴 + 호스트 + 포트) 에서 온 페이지만 현재 리소스를 <iframe> 등에 넣어 보여줄 수 있도록 허용
                .frameOptions(frameOptions -> frameOptions.sameOrigin())); // H2 콘솔을 위한 설정

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        authProvider.setHideUserNotFoundExceptions(false); // 사용자를 찾을 수 없는 예외를 숨기지 않음

        return new ProviderManager(authProvider);
    }
}