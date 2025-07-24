package com.kkh.shopping.Member;

import com.kkh.shopping.Member.CustomUser;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

@Component
public class JwtFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

//        System.out.println("🟢 JwtFilter: " + request.getRequestURI()); // 추가!

        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            filterChain.doFilter(request, response);
            return;
        }

        String jwt = Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals("jwt"))
                .map(Cookie::getValue)
                .findFirst().orElse(null);

        if (jwt == null) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            Claims claims = JwtUtil.extractToken(jwt);
            String username = claims.get("username", String.class);
            String displayName = claims.get("displayName", String.class);
            String[] roles = claims.get("authorities", String.class).split(",");

            var authorities = Arrays.stream(roles).map(SimpleGrantedAuthority::new).toList();
            var user = new CustomUser(username, "", authorities);
            user.displayName = displayName;

            var auth = new UsernamePasswordAuthenticationToken(user, null, authorities);
            auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(auth);
        } catch (Exception e) {
            System.out.println("❌ JWT 검증 실패: " + e.getMessage()); // 로그 추가
            filterChain.doFilter(request, response);
            return;
        }

        filterChain.doFilter(request, response);
    }

//    @Override
//    protected boolean shouldNotFilter(HttpServletRequest request) {
//        return false;
//    }
}


//package com.kkh.shopping.Member;
//
//import io.jsonwebtoken.Claims;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.Cookie;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.AuthorityUtils;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//import java.util.Arrays;
//import java.util.List;
//
//
//@Component
//public class JwtFilter extends OncePerRequestFilter { // 요청마다 1회만 실행됨
//
//    @Override
//    protected void doFilterInternal(
//            HttpServletRequest request, // 유저정보 들어있음
//            HttpServletResponse response,
//            FilterChain filterChain
//    ) throws ServletException, IOException {
//
//        Cookie[] cookies = request.getCookies();
//        if (cookies == null) {
//            filterChain.doFilter(request, response);
//            return;
//        }
//
//        var jwtCookie = "";
//        for (int i = 0; i < cookies.length; i++){
//            if (cookies[i].getName().equals("jwt")){
//                jwtCookie = cookies[i].getValue();
//            }
//        }
//
//        Claims claim;
//        try {
//            claim =  JwtUtil.extractToken(jwtCookie);
//        } catch(Exception e) {
//            filterChain.doFilter(request, response);
//            return;
//        }
////        claim.get("username").toString();
//
//        // 🔧 수정됨: null 체크 추가
//        var usernameObj = claim.get("username");
//        var displayNameObj = claim.get("displayName");
//        var authoritiesObj = claim.get("authorities");
//
//        if (usernameObj == null || displayNameObj == null || authoritiesObj == null) {
//            filterChain.doFilter(request, response);
//            return;
//        }
//
//        var username = usernameObj.toString(); // 🔧 수정됨
//        var displayName = displayNameObj.toString(); // 🔧 수정됨
//        var arr = authoritiesObj.toString().split(","); // 🔧 수정됨
//
////        var arr = claim.get("authorities").toString().split(","); // 권한들
//        var authorities = Arrays.stream(arr).map(a -> new SimpleGrantedAuthority(a)).toList();
//
//        var customUser = new CustomUser(username, "none", authorities);
//        customUser.displayName = displayName;
//
//        var authToken = new UsernamePasswordAuthenticationToken(
//                customUser, // ✨ 중요: principal로 CustomUser 객체 자체를 전달
//                null, // credential (비밀번호)는 null로 설정 (이미 JWT로 인증되었기 때문)
//                authorities
//        );
//        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//
//        SecurityContextHolder.getContext().setAuthentication(authToken);
//
//        AuthorityUtils.commaSeparatedStringToAuthorityList(
//                claim.get("authorities").toString());
//
//
//        //요청들어올때마다 실행할코드
//        filterChain.doFilter(request, response);
//    }
//}
