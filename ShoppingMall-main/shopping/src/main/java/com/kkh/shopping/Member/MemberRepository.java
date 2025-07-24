package com.kkh.shopping.Member;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findBydisplayName(String displayName);
    Optional<Member> findByusername(String username);
}
