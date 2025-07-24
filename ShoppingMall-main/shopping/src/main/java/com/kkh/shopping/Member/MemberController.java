package com.kkh.shopping.Member;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import com.kkh.shopping.Member.JwtUtil;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequiredArgsConstructor // Jpa연결
public class MemberController {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final MemberService memberService;
    private final MyUserDetailsService myUserDetailsService;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    @GetMapping("/register")
        public String register(Authentication auth) {
        // 로그인 되어있을땐 리다이렉트
//        if (auth.isAuthenticated()){
//            return "redirect:/list";
//        }
        return "register.html";
    }

    @PostMapping("/member")
        public String addMember(String username, String password, String displayName) throws Exception{

         memberService.addMember(username, password, displayName);
            return "redirect:/list";
    }

    @GetMapping("/login")
        public String login() {
//        System.out.println(result.get().getdisplayName());
//        myUserDetailsService.loadUserBydisplayName(username);
        return "login.html";
    }

    @PostMapping("/logout")
    public String logout(HttpServletResponse response) {
        Cookie[] expired = {
                new Cookie("jwt", null),
                new Cookie("refresh", null)   // ⬅︎ NEW
        };
        for (Cookie c : expired) {
            c.setMaxAge(0);  c.setPath("/");  c.setHttpOnly(true);
            response.addCookie(c);
        }
        SecurityContextHolder.clearContext();
        return "logout success";
    }

    @GetMapping("/fail")
    public String fail() {
        return "fail.html";
    }

    @GetMapping("/")
    public String page() {
        return "redirect:/list";
    }

//    @GetMapping("/logout")
//    public String logout() {
//        return "redirect:/list";
//    }

    @GetMapping("/my-page")
    public String myPage(Authentication auth) {
//        System.out.println(auth);
//        System.out.println(auth.getName());
//        System.out.println(auth.isAuthenticated());
//        System.out.println(auth.getAuthorities().contains(
//                new SimpleGrantedAuthority("일반유저")
//        )); 컨트롤러에서 유저권한 출력
        CustomUser result = (CustomUser) auth.getPrincipal(); // 이 함수는 타입캐스팅 권장
        return "mypage.html";
    }

    // username가 1인 사용자의 정보 출력
//    @GetMapping("/user/1")
//    @ResponseBody
//    public MemberDto getUser() {
//        var a = memberRepository.findById(1L);
//        var result = a.get();
//        var data = new MemberDto(result.getUsername(), result.getDisplayName());
//        return data;
//    }
    @PostMapping("/login/jwt")
    @ResponseBody
    public String loginJWT(@RequestBody Map<String,String> data,
                           HttpServletResponse response) {

        var authToken = new UsernamePasswordAuthenticationToken(
                data.get("username"), data.get("password"));
        var auth = authenticationManagerBuilder.getObject().authenticate(authToken);
        SecurityContextHolder.getContext().setAuthentication(auth);

        /* ─ 액세스/리프레시 토큰 동시 발급 ─ */
        String access  = JwtUtil.createAccessToken(auth);
        String refresh = JwtUtil.createRefreshToken(auth); // ⬅︎ NEW

        Cookie accCookie = new Cookie("jwt", access);
        accCookie.setHttpOnly(true);
        accCookie.setPath("/");
        accCookie.setMaxAge((int) (JwtUtil.ACCESS_EXP / 1000));
        response.addCookie(accCookie);

        Cookie refCookie = new Cookie("refresh", refresh);  // ⬅︎ NEW
        refCookie.setHttpOnly(true);
        refCookie.setPath("/");
        refCookie.setMaxAge((int) (JwtUtil.REFRESH_EXP / 1000));
        response.addCookie(refCookie);

        return access;   // 프런트에서 바로 쓰려면 JSON으로 감싸도 OK
    }
//    @PostMapping("/login/jwt")
//    @ResponseBody
//    public String loginJWT(@RequestBody Map<String, String> data, HttpServletResponse response) {
//        var authToken = new UsernamePasswordAuthenticationToken(data.get("username"), data.get("password"));
//        var auth = authenticationManagerBuilder.getObject().authenticate(authToken);
//        SecurityContextHolder.getContext().setAuthentication(auth);
//
//        var jwt = JwtUtil.createToken(auth);
//
//        var cookie = new Cookie("jwt", jwt);
//        cookie.setHttpOnly(true);
//        cookie.setMaxAge(60 * 60 * 24); // 24시간
//        cookie.setPath("/"); // 모든 요청에 쿠키 포함
//        response.addCookie(cookie);
////        var jwt = jwt.JwtUtil.createToken(SecurityContextHolder.getContext().getAuthentication());
//////        System.out.println(jwt);
////
////        var cookie = new Cookie("jwt", jwt);
////        cookie.setMaxAge(100000); // 쿠키 유효기간
////        cookie.setHttpOnly(true); // 쿠키를 js로 조작하기 어렵게
////        cookie.setPath("/list"); // 쿠키가 전송될 url
////        response.addCookie(cookie);
//////        System.out.println(jwt);
//////        System.out.println(cookie);
//////        System.out.println(authToken.getName().toString());
//        return jwt;
//    }
@PostMapping("/token/refresh")
@ResponseBody
public Map<String,String> refresh(@CookieValue(value="refresh", required=false) String refreshToken,
                                  HttpServletResponse response) {

    if (refreshToken == null)      // 쿠키 없음
        throw new RuntimeException("Refresh token is missing");

    Claims claims = JwtUtil.extractRefreshToken(refreshToken);

    /* 만료 전 2일 이내면 리프레시 토큰도 갱신 (선택) */
    long remain = claims.getExpiration().getTime() - System.currentTimeMillis();
    boolean rotate = remain < (1000L * 60 * 60 * 48);

    /* 사용자 정보로 Authentication 재생성 */
    var userDetails = myUserDetailsService.loadUserByUsername(
            claims.get("username", String.class));
    var auth = new UsernamePasswordAuthenticationToken(
            userDetails, null, userDetails.getAuthorities());

    String newAccess = JwtUtil.createAccessToken(auth);
    Cookie newAccCookie = new Cookie("jwt", newAccess);
    newAccCookie.setHttpOnly(true);
    newAccCookie.setPath("/");
    newAccCookie.setMaxAge((int) (JwtUtil.ACCESS_EXP/1000));
    response.addCookie(newAccCookie);

    if (rotate) {   // 리프레시 토큰 재발급
        String newRefresh = JwtUtil.createRefreshToken(auth);
        Cookie refCookie = new Cookie("refresh", newRefresh);
        refCookie.setHttpOnly(true);
        refCookie.setPath("/");
        refCookie.setMaxAge((int) (JwtUtil.REFRESH_EXP/1000));
        response.addCookie(refCookie);
    }

    return Map.of("accessToken", newAccess);
}

    @GetMapping("/my-page/jwt")
    @ResponseBody
    String myPageJWT(Authentication auth) {

//        Cookie[] cookies = request.getCookies();
//        System.out.println(cookies[0].getName());
//        System.out.println(cookies[0].getValue());
//        var jwtCookie = "";
//        for (int i = 0; i < cookies.length; i++){
//            if (cookies[i].getName().equals("jwt")){
//                jwtCookie = cookies[i].getValue();
//            }
//        }
//        System.out.println(jwtCookie);

        var user = (CustomUser) auth.getPrincipal();
        System.out.println(user);
        System.out.println(user.displayName);
        System.out.println(user.getAuthorities());
        return "마이페이지데이터";
    }

    @Controller
    @RequestMapping("/admin")
    public class AdminController {

        @GetMapping
        public String adminPage(Authentication auth, Model model) {
            CustomUser user = (CustomUser) auth.getPrincipal();
            if (!user.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
                return "redirect:/list";
            }

            model.addAttribute("adminName", user.username);
            return "adminPage";
        }
    }

}

class MemberDto { //DTO : 데이터 전송 객체
    public String username;
    public String displayName;
    MemberDto(String a, String b) { // constructor
        this.username = a;
        this.displayName = b;
    }
}

// DTO의 장점 : 재사용이 쉬움
// 계산기용, 변환용 함수에만 static 사용
