package com.kkh.shopping.Member;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public void addMember(String username, String password, String displayName) throws Exception{

        var result = memberRepository.findByusername(username);
        var result2 = memberRepository.findBydisplayName(displayName);
        if (result.isPresent()) {
            throw new Exception("이미 존재하는 ID입니다.");
        } else if (result2.isPresent()) {
            throw new Exception("이미 존재하는 닉네임입니다.");
        }
        if (username.length() <= 10 && displayName.length() <= 10 && password.length() <= 20) { // username, displayName은 10자 미만, password는 20자 미만
            Member member = new Member();
            member.setDisplayName(displayName);
            member.setUsername(username);
            var hash = passwordEncoder.encode(password);
            member.setPassword(hash);
            memberRepository.save(member);
        } else {
            throw new Exception("입력하신 값이 조건에 맞지 않습니다.");
        }
    }

}
