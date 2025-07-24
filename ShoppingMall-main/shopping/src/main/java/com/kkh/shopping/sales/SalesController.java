package com.kkh.shopping.sales;

import com.kkh.shopping.Member.CustomUser;
import com.kkh.shopping.Member.Member;
import com.kkh.shopping.Member.MemberRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class SalesController {

    private final SalesRepository salesRepository;
    private final MemberRepository memberRepository;

    @PostMapping("/order")
    String postOrder(@RequestParam String title, @RequestParam Integer price, @RequestParam Integer count, Authentication auth) {
        Sales sales = new Sales();
        sales.setCount(count);
        sales.setPrice(price * count);
        sales.setItemName(title);
        CustomUser user = (CustomUser) auth.getPrincipal();
        var member = new Member();
        member.setId(user.id);
        member.setUsername(user.username);
        sales.setMember(member);
        salesRepository.save(sales);

        if (auth == null) {
            throw new IllegalArgumentException("로그인 하셔야 주문가능합니다.");
        } else if (count == 0 || count == null) {
            throw new IllegalArgumentException("수량을 입력해주세요.");
        } else if (price > 999999999) {
            throw new IllegalArgumentException("999999999원 이하만 주문가능합니다.");
        }

        return "list.html";
    }

//    주문목록
    @GetMapping("/order/all")
    String getOrderAll(Model model) {
//        List<Sales> result = salesRepository.findAll();
//        model.addAttribute("items", result);
        List<Sales> result2 = salesRepository.customFindAll();
        model.addAttribute("items", result2);

        List<SalesDto> salesDtos = new ArrayList<>();
        for (Sales sales : result2) {
            SalesDto salesDto = new SalesDto();
            salesDto.itemName = sales.getItemName();
            salesDto.price = sales.getPrice();
            salesDto.username = sales.getMember().getUsername();
            salesDtos.add(salesDto);
        }

//        var salesDto = new SalesDto();
//        salesDto.itemName = result2.get(0).getItemName();
//        salesDto.price = result2.get(0).getPrice();
//        salesDto.username = result2.get(0).getMember().getusername();

        return "orderlist.html";
    }

}

@Getter
class SalesDto {
    String itemName;
    Integer price;
    String username;
}