package com.kkh.shopping.Item;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> { //Item의 데이터타입
    Page<Item> findPageBy(Pageable page);
    List<Item> findAllByTitleContains(String title);
//    Slice<Item> findPageBy(Pageable page); // 전체 페이지 개수를 세지 않음 - db에 부담을 덜 줌

//    풀텍스트 인덱스 - 쿼리문에도 파라미터문법 사용가능(?1), 뒤에 이어붙이는 식으로 파라미터 여러개 사용 가능
//    match agaainst가 쿼리문 용 풀텍스트 문법
    @Query(value = "select * from item where match(title) against(?1)", nativeQuery = true)
    List<Item> rawQuery1(String text);

    // 페이지네이션
    @Query(value = "SELECT * FROM item WHERE MATCH(title) AGAINST(?1)", countQuery = "SELECT count(*) FROM item WHERE MATCH(title) AGAINST(?1)", nativeQuery = true)
    Page<Item> findPageByTitle(String keyword, Pageable pageable);

}
