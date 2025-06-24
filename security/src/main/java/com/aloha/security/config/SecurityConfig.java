package com.aloha.security.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableWebSecurity              // 해당 클래스를 스프링 시큐리티 설정 빈으로 등록
public class SecurityConfig {

    @Autowired
    private DataSource dataSource;

    // @Autowired
    // private PasswordEncoder passwordEncoder;


    // 🔐 스프링 시큐리티 설정 메소드
	@Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        // ✅ 인가 설정
        http.authorizeHttpRequests(auth -> auth
                                .requestMatchers("/admin", "/admin/**").hasRole("ADMIN")
                                .requestMatchers("/user", "/user/**").hasAnyRole("USER","ADMIN")
                                .requestMatchers("/**").permitAll()
                                .anyRequest().permitAll()
                                );

        // 🔐 폼 로그인
        http.formLogin(login -> login.permitAll());

        // 🔄 자동 로그인
        http.rememberMe(me -> me
                .key("aloha")
                .tokenRepository(tokenRepository())
                .tokenValiditySeconds(60 * 60 * 24 * 7));

        return http.build();
    }

    // PersistentRepository 토큰정보 객체 - 빈 등록
    @Bean
    public PersistentTokenRepository tokenRepository() {
        // JdbcTokenRepositoryImpl : 토큰 저장 데이터 베이스를 등록하는 객체
        JdbcTokenRepositoryImpl repositoryImpl = new JdbcTokenRepositoryImpl();
        // 토큰 저장소를 사용하는 데이터 소스 지정
        repositoryImpl.setDataSource(dataSource);
        // persistent_logins 테이블 자동 생성
        // repositoryImpl.setCreateTableOnStartup(true);
        try {
            repositoryImpl.getJdbcTemplate().execute(JdbcTokenRepositoryImpl.CREATE_TABLE_SQL);
        } catch (Exception e) {
            log.error("persistent_logins 테이블이 이미 생성되었습니다.");
        }
        return repositoryImpl;
    }


    // 👮‍♂️🔐사용자 인증 관리 메소드
    // 인메모리 방식으로 인증
    // @Bean
    // public UserDetailsService userDetailsService() {
    //     // user 123456
    //     UserDetails user = User.builder()
    //                             .username("user")
    //                             .password(passwordEncoder.encode("123456"))
    //                             .roles("USER")
    //                             .build();
    //     // admin 123456
    //     UserDetails admin = User.builder()
    //                             .username("admin")
    //                             .password(passwordEncoder.encode("123456"))
    //                             .roles("USER", "ADMIN")
    //                             .build();

    //     return new InMemoryUserDetailsManager( user, admin );
    //     // return new JdbcUserDetailsManager( ... );
    // }


    /**
     * 🍃 JDBC 인증 방식 빈 등록
     * @return
     */
    @Bean
    public UserDetailsService userDetailsService() {
        JdbcUserDetailsManager userDetailsManager
                = new JdbcUserDetailsManager(dataSource);

        // 사용자 인증 쿼리
        String sql1 = " SELECT username, password, enabled "
                    + " FROM user "
                    + " WHERE username = ? "
                    ;
        // 사용자 권한 쿼리
        String sql2 = " SELECT username, auth "
                    + " FROM user_auth "
                    + " WHERE username = ? "
                    ;
        userDetailsManager.setUsersByUsernameQuery(sql1);
        userDetailsManager.setAuthoritiesByUsernameQuery(sql2);
        return userDetailsManager;
    }


    /**
     * 🍃 AuthenticationManager 인증 관리자 빈 등록
     * @param authenticationConfiguration
     * @return
     * @throws Exception
    */
    @Bean
    public AuthenticationManager authenticationManager(
                                    AuthenticationConfiguration authenticationConfiguration ) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }


}
