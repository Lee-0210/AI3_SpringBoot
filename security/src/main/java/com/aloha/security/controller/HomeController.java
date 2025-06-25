package com.aloha.security.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.aloha.security.domain.CustomUser;
import com.aloha.security.domain.Users;
import com.aloha.security.service.UserService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class HomeController {

    @Autowired
    private UserService userService;

    @GetMapping("")
    public String home(@AuthenticationPrincipal CustomUser authUser, Model model) throws Exception {
        log.info(":::::::::: 메인 화면 ::::::::::");

        // if (principal != null) {
        // String username = principal.getName(); // 인증된 사용자 아이디
        // log.info("username : {}", username);
        // Users user = userService.select(username); // 사용자 정보 조회
        // log.info("user : {}", user);
        // model.addAttribute("user", user); // 사용자 정보 모델에 등록
        // }

        // if (authentication != null) {
        // User user = (User) authentication.getPrincipal(); // 인증된 사용자 아이디
        // String username = user.getUsername(); // 인증된 사용자 아이디
        // String password = user.getPassword(); // 인증된 사용자 비밀번호
        // Collection<GrantedAuthority> authList = user.getAuthorities();

        // log.info("username : {}", username);
        // log.info("password : {}", password);
        // log.info("authList : {}", authList);

        // Users joinedUser = userService.select(username); // 사용자 정보 조회
        // log.info("joinedUser : {}", joinedUser);
        // model.addAttribute("user", user); // 사용자 정보 모델에 등록
        // }

        if (authUser != null) {
            log.info("authUser : {}", authUser);
            Users user = authUser.getUser();
            model.addAttribute("user", user);
        }

        return "index";
    }

    /**
     * 회원 가입 화면
     *
     * @return
     */
    @GetMapping("/join")
    public String join() {
        return "join";
    }

    /**
     * 회원 가입 처리
     *
     * @param user
     * @return
     * @throws Exception
     */
    @PostMapping("/join")
    public String joinPost(Users user, HttpServletRequest request) throws Exception {
        // 암호화 전 비밀 번호
        String plainPassword = user.getPassword();
        // 회원 가입 요청
        int result = userService.join(user);
        // 회원 가입 성공 시, 바로 로그인
        boolean loginResult = false;
        if (result > 0) {
            // 암호화 전 비밀번호로 다시 세팅
            user.setPassword(plainPassword);
            loginResult = userService.login(user, request); // 바로 로그인
        }
        if (loginResult)
            return "redirect:/"; // 메인 화면으로 이동
        if (result > 0)
            return "redirect:/login"; // 로그인 화면으로 이동

        return "redirect:/join?error=true";
    }

    /**
     * 로그인 화면
     *
     * @return
     */
    @GetMapping("/login")
    public String login(@CookieValue(value = "remember-id", required = false) Cookie cookie, Model model) {
        log.info(":::::::::: 로그인 페이지 ::::::::::");
        String username = "";
        boolean rememberId = false;

        if (cookie != null) {
            log.info("CookieName : {}", cookie.getName());
            log.info("CookieValue : {}", cookie.getValue());
            username = cookie.getValue();
            rememberId = true;
        }

        model.addAttribute("username", username);
        model.addAttribute("rememberId", rememberId);
        return "login";
    }

}
