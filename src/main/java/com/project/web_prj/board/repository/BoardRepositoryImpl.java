package com.project.web_prj.board.repository;

import com.project.web_prj.board.domain.Board;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository("bri")
@Log4j2
//@RequiredArgsConstructor // 롬복못쓸경우
public class BoardRepositorylmpl implements BoardRepository{

    private final JdbcTemplate template;

    @Autowired
    public BoardRepositorylmpl(JdbcTemplate template) {
        this.template = template;
    }

    @Override
    public boolean save(Board board) {
        log.info("save process with jdbc - {}", board);
        String sql = "INSERT INTO tbl_board " +
                    "(board_no, writer, title, content) " +
                    "VALUES (seq_tbl_board.nextval, ?, ?, ?)";
        return template.update(sql
                , board.getWriter()
                , board.getTitle()
                , board.getContent()
        ) == 1;
    }

    @Override
    public List<Board> findAll() {
        String sql = "SELECT * FROM tbl_board " +
                "ORDER BY board_no DESC";
        return template.query(sql, (rs, rn) -> new Board(rs));
    }

    @Override
    public Board findOne(Long boardNo) {
        String sql = "SELECT * FROM tbl_board " +
                "WHERE board_no=?"; // queryFor0bject는 리턴타입이 T 아무거나
        return template.queryForObject(sql, (rs, rn) -> {

            return new Board(rs);
        }, boardNo);
    }

    @Override
    public boolean remove(Long boardNo) {
        String sql = "DELETE FROM tbl_board WHERE board_no=?";
        return template.update(sql, boardNo) == 1;
    }

    @Override
    public boolean modify(Board board) {
        String sql = "UPDATE tbl_board " +
                "SET writer = ?, title=?, content=? " +
                "WHERE board_no=?";
        return template.update(sql,
                board.getWriter(),
                board.getTitle(),
                board.getContent(),
                board.getBoardNo()
                ) == 1;
    }

    @Override
    public int getTotalCount() {
        String sql = "SELECT COUNT(*) AS cnt FROM tbl_board";
        return template.queryForObject(sql, Integer.class);
    }
}
