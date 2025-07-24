package com.kkh.shopping.Board;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class BoardController {

    private final BoardRepository boardRepository; // db입출력을 위해 함수 생성

    @GetMapping("/board")
    String board(Model model) {
        boardRepository.findAll();
        List<Board> result = boardRepository.findAll();
        model.addAttribute("boards", result);

        return "title.html";
    }
}
