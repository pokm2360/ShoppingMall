package com.kkh.shopping.Item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor // JPA 연결
public class ItemService { // 서비스는 db입출력, 검사등 비즈니스 로직모음

    private final ItemRepository itemRepository;

    // 상품 저장
    public void saveItem(String title, Integer price) {
        Item item = new Item();
        item.setTitle(title); // 변수들을 따로 정의해야함
        item.setPrice(price);
        itemRepository.save(item);
    }

    // 상품 리스트
    public void list(Model model) {
        List<Item> result = itemRepository.findAll();
        model.addAttribute("items", result);
    }

    // 상품 상세페이지
    public void detail(Long id, Model model) {
        Optional<Item> result = itemRepository.findById(id);
        if (result.isPresent()) {
            Item item = result.get();

            // 상품명 길이 제한 (예: 50자 이상은 예외 처리)
            if (item.getTitle().length() > 50) {
                System.out.println("상품명이 너무 깁니다.");
            }

            // 가격 검증 (예: 음수 또는 1억 이상이면 예외 처리)
            if (item.getPrice() < 0 && item.getPrice() > 100000000) {
                System.out.println("상품 가격을 바르게 입력해주세요.");
            }

            model.addAttribute("data", item);
        } else {
            System.out.println("해당 상품은 존재하지 않습니다.");
        }
    }

    // 상품 수정페이지 접속
    public void edit(Long id, Model model) {
        Optional<Item> result = itemRepository.findById(id);
        if (result.isPresent()) {
            model.addAttribute("data", result.get());
        } else {
            throw new IllegalArgumentException("해당 ID의 상품을 찾을 수 없습니다.");
        }
    }


    // 상품 정보 수정
    public void editItem(Long id, String title, Integer price) {
        // 예외 처리: 이름이 100자 초과하거나 가격이 음수일 경우
        if (title.length() > 100) {
            throw new IllegalArgumentException("상품 이름은 100자 이하여야 합니다.");
        }
        if (price < 0) {
            throw new IllegalArgumentException("상품 가격은 0 이상이어야 합니다.");
        }

        // 기존 아이템 조회 후 수정
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 상품을 찾을 수 없습니다."));

        item.setTitle(title);
        item.setPrice(price);
        itemRepository.save(item);
    }

    // 상품 정보 삭제
    public void deleteItem(Long id) {
        Optional<Item> item = itemRepository.findById(id);
       if (item.isPresent()) {
           itemRepository.deleteById(id);
       } else {
           throw new IllegalArgumentException("해당 상품은 존재하지 않습니다.");
       }
    }

}
