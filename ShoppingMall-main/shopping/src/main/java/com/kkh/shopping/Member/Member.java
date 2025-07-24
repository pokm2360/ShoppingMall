package com.kkh.shopping.Member;

import com.kkh.shopping.sales.Sales;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Setter
@Getter
@ToString
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(unique = true)
    public String username; // id

    @Column(nullable = false)
    public String password; // pw

    @Column(unique = true)
    public String displayName; // 닉네임

    @Column(nullable = false)
    private String role = "ROLE_USER"; // 기본값

    public boolean isAdmin() {
        return "ROLE_ADMIN".equals(role);
    }
    
//    member 행 출력시 그 멤버가 기록된 Sales 행도 출력
    @ToString.Exclude
    @OneToMany(mappedBy = "member")
    List<Sales> sales = new ArrayList<>();
}
