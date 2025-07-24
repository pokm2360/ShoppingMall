package com.kkh.shopping.Member;

import com.kkh.shopping.Member.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.Map;
//
//@RestController
//@RequiredArgsConstructor
//public class AuthController {
//
//    private final AuthenticationManager authenticationManager;
//
//    @PostMapping("/login/jwt")
//    public String login(@RequestBody Map<String, String> data, HttpServletResponse response) {
//        var authToken = new UsernamePasswordAuthenticationToken(data.get("username"), data.get("password"));
//        var auth = authenticationManager.authenticate(authToken);
//        SecurityContextHolder.getContext().setAuthentication(auth);
//
//        var jwt = JwtUtil.createToken(auth);
//        var cookie = new Cookie("jwt", jwt);
//        cookie.setHttpOnly(true);
//        cookie.setMaxAge(60 * 60 * 2); // 2 hours
//        cookie.setPath("/");
//        response.addCookie(cookie);
//
//        return jwt;
//    }
//}

