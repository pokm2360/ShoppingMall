package com.kkh.shopping.sales;

import com.kkh.shopping.Member.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString
public class Sales {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String itemName;

    private Integer price;

    @ManyToOne(fetch = FetchType.LAZY) // LAZY : 필요할때만 행을 가져옴
    @JoinColumn(name = "member_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Member member;

    private Integer count; // 수량

    @CreationTimestamp // 행 추가할때 자동으로 시간채워짐
    private LocalDateTime ordertime; // 주문시간
}
