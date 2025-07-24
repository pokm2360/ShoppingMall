package com.kkh.shopping.config;

import com.kkh.shopping.Member.JwtFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable());
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/login/jwt", "/register", "/list","/token/refresh", "/**").permitAll()
                .anyRequest().authenticated()
        );

        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}

//package com.kkh.shopping;
//
//import com.kkh.shopping.Member.JwtFilter;
//import jakarta.annotation.security.PermitAll;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.access.ExceptionTranslationFilter;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//import org.springframework.security.web.csrf.CsrfTokenRepository;
//import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
//
//@Configuration
//@EnableWebSecurity
//public class SecurityConfig {
//
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http.csrf(csrf -> csrf.disable());
//
//        // 세션 설정: STATELESS를 제거하거나 IF_REQUIRED로 변경
//        http.sessionManagement(session ->
//                session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED) // 이 부분 변경
//        );
//
//        http.authorizeHttpRequests(authorize ->
//                authorize
//                        .requestMatchers("/admin/**").hasRole("ADMIN")
//                        .requestMatchers("/login/jwt", "/list", "/register", "/comment", "/**").permitAll()
//                        .anyRequest().authenticated() // 나머지 모든 요청은 인증 필요
//        );
//
//        // JWT 필터 등록 (UsernamePasswordAuthenticationFilter.class 앞이 일반적)
//        http.addFilterBefore(new JwtFilter(), UsernamePasswordAuthenticationFilter.class);
//
//        http.formLogin(formLogin ->
//                formLogin
//                        .loginPage("/login")
//                        .defaultSuccessUrl("/")
//                        .failureUrl("/fail")
//                        .permitAll()
//        );
//
//        http.logout(logout ->
//                logout
//                        .logoutUrl("/logout")
//                        .logoutSuccessUrl("/list")
//                        .invalidateHttpSession(true)
//                        .deleteCookies("JSESSIONID")
//                        .permitAll()
//        );
//
//        return http.build();
//    }
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//}


//세션 방식
//@Configuration
//@EnableWebSecurity
//public class SecurityConfig {
//
//    @Bean
//    public CsrfTokenRepository csrfTokenRepository() {
//        HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
//        repository.setHeaderName("X-XSRF-TOKEN");
//        return repository;
//    }
//
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception { // 어떤 페이지를 로그인검사할지 설정
//        http.csrf((csrf) -> csrf.disable()); // csrf기능을 끈 것 = csrf는 외부공격
////        http.csrf(csrf -> csrf.csrfTokenRepository(csrfTokenRepository())
////                .ignoringRequestMatchers("/**")
////                "/login","/list", "/regis/ter", "/comment"
////        );
//
////        세션 데이터 생성x
//        http.sessionManagement((session) -> session
//                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//        );
////      관리자 접근제어 설정
//        http.authorizeHttpRequests((authorize) ->
//                authorize
//                        .requestMatchers("/admin/**").hasRole("ADMIN")
//                        .anyRequest().permitAll()
//        );
//
//        http.addFilterBefore(new JwtFilter(), ExceptionTranslationFilter.class); // 특정 필터 전에 실행
//
//
//        http.authorizeHttpRequests((authorize) ->
//                authorize.requestMatchers("/**").permitAll() //permitAll은 항상 허용
//        );
//
//        http.formLogin((formLogin) // form을 사용해서 로그인 url은 /login
//                    -> formLogin.loginPage("/login")
//                    .defaultSuccessUrl("/") // 성공일때 리다이렉트
//                    .failureUrl("/fail")); // 실패일때 리다이렉트
//
//
//        http.logout(logout -> logout.logoutUrl("/logout")
//                .logoutSuccessUrl("/list") // 로그아웃 후 이동할 페이지
//                .invalidateHttpSession(true)  // 세션 제거
//                .deleteCookies("JSESSIONID")); // 쿠키 제거
//        return http.build();
//
//    }
//
//    @Bean
//    PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//}

