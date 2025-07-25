package com.kkh.shopping.comment;

import com.kkh.shopping.Member.CustomUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class CommentController {

    private final CommentRepository commentRepository;

    @PostMapping("/comment")
    String postCommnet(@RequestParam String content, @RequestParam Long parent, Authentication auth) { // auth에는 로그인한 유저의 정보가 들어있음
        CustomUser user = (CustomUser) auth.getPrincipal();
        var data = new Comment();
        data.setContent(content);
        data.setUsername(user.getUsername());
        data.setParentId(parent);
        commentRepository.save(data);
        return "redirect:/list";
    }
}
