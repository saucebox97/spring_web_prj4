package com.project.web_prj.board.domain;

import lombok.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

@Setter @Getter @ToString @EqualsAndHashCode
@NoArgsConstructor @AllArgsConstructor
public class Board {

    // 테이블 컬럼 필드
    private Long boardNo; // Long(래퍼)타입이면 초기값 null 기본타입이면 0
    private String writer;
    private String title;
    private String content;
    private Long viewCnt; // 10자리수니까 100억 그러니 long
    private Date regDate;


    // 커스텀 데이터 필드
    private String shortTitle; // 줄임 제목
    private String prettierDate; // 변경된 날짜포맷 문자열
    private boolean newArticle; // 신규게시물 여부



    // rs를 해야 repository에서 rs로짧게함
    public Board(ResultSet rs) throws SQLException {
        this.boardNo = rs.getLong("board_no");
        this.title = rs.getString("title");
        this.writer = rs.getString("writer");
        this.content = rs.getString("content");
        this.viewCnt = rs.getLong("view_cnt");
        this.regDate = rs.getTimestamp("reg_date");//시분초까지읽음
    }
}
