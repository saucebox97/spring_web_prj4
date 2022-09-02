
CREATE SEQUENCE seq_tbl_reply;
-- 게시글과 댓글과의  1:M // M이 1이란 부모의 fk를 참조
CREATE TABLE tbl_reply (
    reply_no NUMBER(10),
    reply_text VARCHAR2(1000) NOT NULL,
    reply_writer VARCHAR2(50) NOT NULL,
    reply_date DATE default SYSDATE,
    board_no NUMBER(10),
    CONSTRAINT pk_reply PRIMARY KEY (reply_no),
    CONSTRAINT fk_reply_board
    FOREIGN KEY (board_no)
    REFERENCES tbl_board (board_no)
);

-- Maria DB
CREATE TABLE tbl_reply (
    reply_no INT(10) AUTO_INCREMENT,
    reply_text VARCHAR(1000) NOT NULL,
    reply_writer VARCHAR(50) NOT NULL,
    reply_date DATETIME default current_timestamp,
    board_no INT(10),
    CONSTRAINT pk_reply PRIMARY KEY (reply_no),
    CONSTRAINT fk_reply_board
    FOREIGN KEY (board_no)
    REFERENCES tbl_board (board_no)
);