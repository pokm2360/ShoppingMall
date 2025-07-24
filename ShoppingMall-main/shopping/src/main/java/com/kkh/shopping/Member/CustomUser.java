package com.kkh.shopping.Member;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class CustomUser extends User {
    public Long id;
    public String username;
    public String displayName;

    public CustomUser(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.username = username;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    public String getLoginId() {
        return this.username;
    }

}

//package com.kkh.shopping.Member;
//
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.userdetails.User;
//
//import java.util.Collection;
//
//public class CustomUser extends User { // extends : User클래스를 복사
//    public Long id;
//    public String username;
//    public String displayName;
//    public CustomUser(String username, String password, Collection<? extends GrantedAuthority> authorities) {
//        super(username, password, authorities); // super는 복사한 클래스, ()은 extends로 복사한 클래스의 instructor
//        this.username = username;
//    }
//
//    // sec:authentication="principal.username"이 호출할 메서드를 오버라이드
//    @Override
//    public String getUsername() {
//        // 화면에 표시하고 싶은 이름을 반환
//        // 여기서는 displayName을 반환하도록 설정
//        return this.displayName;
//    }
//
//    // 필요하다면 실제 로그인 ID를 가져오는 다른 메서드를 추가
//    public String getLoginId() {
//        return this.username;
//    }
//}
