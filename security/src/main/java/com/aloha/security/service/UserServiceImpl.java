package com.aloha.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aloha.security.domain.UserAuth;
import com.aloha.security.domain.Users;
import com.aloha.security.mapper.UserMapper;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserMapper userMapper;

    @Autowired
    PasswordEncoder passwordEncoder;

    /**
     * 회원가입
     * 1. 비밀번호를 암호화
     * 2. 회원 등록
     * 3. 기본 권한을 등록
     */
    @Override
    @Transactional // 트랜잭션 처리를 설정 (회원정보, 회원권한)
    public int join(Users user) throws Exception {
        String username = user.getUsername();
        String password = user.getPassword();
        String encodedPassword = passwordEncoder.encode(password); // 🔒 비밀번호 암호화
        user.setPassword(encodedPassword);

        // 회원 등록
        int result = userMapper.join(user);

        if (result > 0) {
            // 회원 기본 권한 등록
            UserAuth userAuth = new UserAuth();
            userAuth.setUsername(username);
            userAuth.setAuth("ROLE_USER");
            result = userMapper.insertAuth(userAuth);
        }
        return result;
    }

    @Override
    public int insertAuth(UserAuth userAuth) throws Exception {
        int result = userMapper.insertAuth(userAuth);
        return result;
    }

}
