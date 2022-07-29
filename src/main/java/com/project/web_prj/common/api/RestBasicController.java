package com.project.web_prj.common.api;

import com.project.web_prj.board.domain.Board;
import lombok.extern.log4j.Log4j2;
import org.apache.ibatis.annotations.Delete;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

// jsp 뷰포워딩을 하지않고 클라이언트에게 JSON데이터를 전송함
// JSON(제이슨)이란 ? 자바스크립트의 표기법을 따온거지 자바스크립트가 아니다
@RestController
@Log4j2
public class RestBasicController {

    @GetMapping("/api/hello")
    public String hello() {
        return "hello!!!";
    }
    @GetMapping("/api/board")
    public Board board() {
        Board board = new Board();
        board.setBoardNo(10L);
        board.setContent("할롱~");
        board.setTitle("메롱~~");
        board.setWriter("박영희");
        return board;
    }
    @GetMapping("/api/arr")
    public String[] arr() {
        String[] foods = {"짜장면", "레몬에이드", "볶음밥"};
        return foods;
    }

    // post 요청처리 // 제이슨을받는다 // 제이
    @PostMapping("/api/join") // 요청 Body안에있는 json을 읽어라 없으면 리퀘스트파람읽음
    public String join(@RequestBody List<String> info) {
        log.info("/api/join POST!! - {}", info);
        return "POST-OK"; // 클라이언트한테 제이슨을보내는방법
    }

    // put 요청처리
    @PutMapping("/api/join") //클라이언트에서 일로주는것 제이슨을받을때
    public String joinPut(@RequestBody Board board) {
        log.info("/api/join Put!! - {}",board);
        return "PUT-OK";
    }

    // delete 요청처리
    @DeleteMapping("/api/join")
    public String joinDelete() {
        log.info("/api/join Delete!!");
        return "DEL-OK";
    }

    // RestController에서 뷰포워딩하기
    @GetMapping("/hoho")
    public ModelAndView hoho() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("index");
        return mv;
    }

}
