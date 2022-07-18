package com.project.web_prj.board.controller;

import com.project.web_prj.board.domain.Board;
import com.project.web_prj.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/*

 */

@Controller
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {

    private final BoardService boardService;

    // 게시물 목록 요청
    @GetMapping("/list")
    public String list(Model model) {
        log.info("controller request /board/list GET!");
        List<Board> boardList = boardService.findAllService();
        log.info("return data - {}", boardList);

        model.addAttribute("bList", boardList);
        return "board/board-list";
    }

    // 게시물 상세 조회 요청 // PathVariable URL 경로에 변수를 넣어줌
    @GetMapping("/content/{boardNo}")
    public String content(@PathVariable Long boardNo, Model model) {
        log.info("controller request /board/content GET! - {}", boardNo);
        Board board = boardService.findOneService(boardNo);
        log.info("return data - {}", board);
        model.addAttribute("b", board);
        return "board/board-detail";
    }

    // 게시물 쓰기 화면 요청
    @GetMapping("/write")
    public String write() {
        log.info("controller request /board/write GET!");
        return "board/board-write";
    }

    // 게시물 등록 요청
    @PostMapping("/write")
    public String write(Board board) {
        log.info("controller request /board/write POST! - {}", board);
        boolean flag = boardService.saveService(board);
        return flag ? "redirect:/board/list" : "redirect:/";
    }

}
