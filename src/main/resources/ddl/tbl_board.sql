CREATE SEQUENCE seq_tbl_board;
DROP SEQUENCE seq_tbl_board;

DROP TABLE tbl_board;
CREATE TABLE tbl_board (
    board_no NUMBER(10),
    writer VARCHAR2(20) NOT NULL,
    title VARCHAR2(200) NOT NULL,
    content VARCHAR2(2000),
    view_cnt NUMBER(10) DEFAULT 0,
    reg_date DATE DEFAULT SYSDATE,
    CONSTRAINT pk_tbl_board PRIMARY KEY (board_no)
);

SELECT *
from tbl_board
order by board_no DESC
;

------

-- ROWNUM 은 조회된 순서대로 순번
SELECT ROWNUM, tbl_board.*
FROM tbl_board
WHERE ROWNUM BETWEEN 1 AND 20
ORDER BY board_no DESC
;
-- 로우넘을 마지막에하면 안됌 순번이 섞여 버림
-- 1 먼저 역정렬을함  2 로우넘 3 로우넘을 걸려냄
SELECT *
FROM (SELECT ROWNUM rn, v_board.*
        FROM (
                SELECT *
                FROM tbl_board
                ORDER BY board_no DESC
                ) v_board)
WHERE rn BETWEEN 11 AND 20
;

-----------------------------------------

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

-- 첨부파일 정보를 가지는 테이블 생성
CREATE TABLE file_upload (
    file_name VARCHAR2(150), -- 2022/08/01/asdfsdfsadf_상어.jpg
    reg_date DATE DEFAULT SYSDATE,
    bno NUMBER(10) NOT NULL
);

-- PK, FK 부여
ALTER TABLE file_upload
ADD CONSTRAINT pk_file_name
PRIMARY KEY (file_name);

ALTER TABLE file_upload
ADD CONSTRAINT fk_file_upload
FOREIGN KEY (bno)
REFERENCES tbl_board (board_no)
ON DELETE CASCADE; -- 게시물지워지면 첨부파일 다지워짐

------------------------------
-- 회원 관리 테이블
CREATE TABLE tbl_member (
    account VARCHAR2(50),
    password VARCHAR2(150) NOT NULL,
    name VARCHAR2(50) NOT NULL,
    email VARCHAR2(100) NOT NULL UNIQUE,
    auth VARCHAR2(20) DEFAULT 'COMMON',
    reg_date DATE DEFAULT SYSDATE,
    CONSTRAINT pk_member PRIMARY KEY (account)
);



SELECT * FROM tbl_member;

ALTER TABLE tbl_board ADD account VARCHAR2(50) NOT NULL;
ALTER TABLE tbl_reply ADD account VARCHAR2(50) NOT NULL;