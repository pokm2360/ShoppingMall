package com.kkh.shopping.Item;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@ToString // ToString함수를 알아서 만들어줌 -- Object(db에 저장된)의 변수들을 한번에 출력해줌
@Getter
@Setter
@Table(indexes = @Index(columnList = "title", name = "index1"))
public class Item {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto Increment 기능
    private Long Id;

    private String title;
    private Integer price; // 컬럼용 변수는 무조건 Integer / Long은 900경까지 저장 가능

//    public String getTitle() {
//        return title;
//    } -- getter : private이어도 다른 클래스에서 title 변수를 사용할 수 있게 함
    
//    public void setTitle(String title) {
//        this.title = title;
//    } -- setter : private 함수를 수정할 수 있게 함
}

//class 클래스1 extends Item { // 상속: 클래스를 복사
//
//}
//(columnDefinition = "TEXT")-컬럼 타입 지정

//class 클래스1 extends Item {
//    
//} -- 상속


//1. 테이블 하나 필요하면 @Entity 붙은 class 하나 만들면 자동으로 생성가능
//2. 그 class에 변수 만들면 그게 자동으로 컬럼으로 변합니다.
//3. @id 컬럼은 항상 하나 있는게 좋습니다. @GeneratedValue 넣는 것도 편함
//4. @Column 사용해서 컬럼마다 제약사항을 집어넣을 수 있습니다.

// privete : 같은 클래스 안에서만 쓸 수 있음
// protected: package-private와 같음 / 상속한 클래스는 맘대로 사용 가능
// public : 다른 클래스에서도 쓸 수 있음
// static : 클래스.변수 이런식으로 직접 사용가능, 유틸리티형 함수 만들때 사용 ex) Item.title