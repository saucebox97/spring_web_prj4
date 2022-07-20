package com.project.web_prj.board.repository;


import com.project.web_prj.board.domain.Board;
import com.project.web_prj.common.paging.Page;
import com.project.web_prj.common.search.Search;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BoardMapper {

    boolean save(Board board);

    // 게시글 전체 조회 with search
    List<Board> findAll(Page page);
    List<Board> findAll2(Search search);

    // 게시글 상세 조회
    Board findOne(Long boardNo);

    // 게시글 삭제
    boolean remove(Long boardNo);

    // 게시글 수정
    boolean modify(Board board);

    // 전체 게시물 수 조회
    int getTotalCount();

    // 조회수 상승 처리
    void upViewCount(Long boardNo);

}
