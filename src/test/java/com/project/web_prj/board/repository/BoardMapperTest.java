package com.project.web_prj.board.repository;

import com.project.web_prj.board.domain.Board;
import com.project.web_prj.common.paging.Page;
import com.project.web_prj.common.search.Search;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BoardMapperTest {

    @Autowired
    BoardMapper mapper;

    @Test
    @DisplayName("등록")
    void saveTest() {
        Board board = new Board();
        board.setWriter("111");
        board.setTitle("111");
        board.setContent("111");

        mapper.save(board);
    }

    @Test
    @DisplayName("전체확인")
    void findAll() {
        List<Board> boardList = mapper.findAll(new Page(1,10));
        for (Board b : boardList) {
            System.out.println(b);
        }

        assertEquals(10, boardList.size());
    }

    @Test
    @DisplayName("특정 게시물 조회하고 글제목이 일치")
    void findOneTest() {

        Board board = mapper.findOne(319L);
        System.out.println("board = " + board);

        assertEquals("111", board.getTitle());

    }

//    @Test
//    @DisplayName("제목으로 검색된 목록을 조회해야 한다.")
//    void searchByTitleTest() {
//
//        Search search = new Search(new Page(1, 10)
//                , "tc", "우왁");
//
//        mapper.findAll2(search).forEach(System.out::println);
//    }

}