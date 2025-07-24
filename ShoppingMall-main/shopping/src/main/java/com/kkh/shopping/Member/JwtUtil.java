package com.kkh.shopping.Member;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class JwtUtil {

    /* ────────── ① 키 & 만료시간 분리 ────────── */
    private static final SecretKey ACCESS_KEY =
            Keys.hmacShaKeyFor(Decoders.BASE64.decode("jwtpassword123jwtpassword123jwtpassword123jwtpassword123jwtpassword"));
    private static final SecretKey REFRESH_KEY =                              // ⬅︎ NEW
            Keys.hmacShaKeyFor(Decoders.BASE64.decode("jwtrefresh123jwtrefresh123jwtrefresh123jwtrefresh123"));
    public static final long  ACCESS_EXP   = 1000L * 60 * 60 * 2;            // 2 h
    public static final long  REFRESH_EXP  = 1000L * 60 * 60 * 24 * 14;      // 14 d

    /* ────────── ② 액세스 / 리프레시 토큰 개별 발급 ────────── */
    public static String createAccessToken(Authentication auth) {
        var user        = (CustomUser) auth.getPrincipal();
        var authorities = auth.getAuthorities().stream()
                .map(a -> a.getAuthority()).collect(Collectors.joining(","));
        return Jwts.builder()
                .claim("username",     user.getLoginId())
                .claim("displayName",  user.displayName)
                .claim("authorities",  authorities)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + ACCESS_EXP))
                .signWith(ACCESS_KEY)
                .compact();
    }

    public static String createRefreshToken(Authentication auth) {            // ⬅︎ NEW
        var user = (CustomUser) auth.getPrincipal();
        return Jwts.builder()
                .claim("username", user.getLoginId())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + REFRESH_EXP))
                .signWith(REFRESH_KEY)
                .compact();
    }

    /* ────────── ③ 파싱 메서드도 2개 ────────── */
    public static Claims extractToken(String token) {
        return Jwts.parser().verifyWith(ACCESS_KEY).build()
                .parseSignedClaims(token).getPayload();
    }
    public static Claims extractRefreshToken(String token) {                  // ⬅︎ NEW
        return Jwts.parser().verifyWith(REFRESH_KEY).build()
                .parseSignedClaims(token).getPayload();
    }

}

//@Component
//public class JwtUtil {
//    static final SecretKey key =
//            Keys.hmacShaKeyFor(Decoders.BASE64.decode("jwtpassword123jwtpassword123jwtpassword123jwtpassword123jwtpassword"));
//
//    public static String createToken(Authentication auth) {
//        var user = (CustomUser) auth.getPrincipal();
//        var authorities = auth.getAuthorities().stream().map(a -> a.getAuthority()).collect(Collectors.joining(","));
//
//        return Jwts.builder()
//                .claim("username", user.getLoginId())
//                .claim("displayName", user.displayName)
//                .claim("authorities", authorities)
//                .issuedAt(new Date())
//                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 2))
//                .signWith(key)
//                .compact();
//    }
//
//    public static Claims extractToken(String token) {
//        return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
//    }
//}

//package com.kkh.shopping.Member;
//
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.io.Decoders;
//import io.jsonwebtoken.security.Keys;
//import org.springframework.security.core.Authentication;
//import org.springframework.stereotype.Component;
//
//import javax.crypto.SecretKey;
//import java.util.Date;
//import java.util.stream.Collectors;
//
//@Component
//public class JwtUtil {
//
//    static final SecretKey key =
//            Keys.hmacShaKeyFor(Decoders.BASE64.decode(
//                    "jwtpassword123jwtpassword123jwtpassword123jwtpassword123jwtpassword"
//            ));
//
//    // JWT 만들어주는 함수
//    public static String createToken(Authentication auth) {
//        var user = (CustomUser) auth.getPrincipal();
//        var authorities = auth.getAuthorities().stream().map(a -> a.getAuthority()).collect(Collectors.joining(","));
//
//        String jwt = Jwts.builder()
//                .claim("username", user.getUsername())
//                .claim("displayName", user.displayName)
//                .claim("authorities", authorities) // 문자만 집어넣을 수 있음
////                권한 넣기
//                .issuedAt(new Date(System.currentTimeMillis()))
//                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 2)) //유효기간 2시간
//                .signWith(key) // 해싱할때 넣을 비번
//                .compact();
//        return jwt;
//    }
//
//    // JWT를 문자로 까주는 함수
//    public static Claims extractToken(String token) {
//        Claims claims = Jwts.parser().verifyWith(key).build()
//                .parseSignedClaims(token).getPayload();
//        return claims;
//    }
//
//}
