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

-- Maria DB
CREATE TABLE tbl_board (
    board_no INT(10) AUTO_INCREMENT, -- AUTO_INCREMENT 시퀀스역할함 /넘버가 int
    writer VARCHAR(20) NOT NULL, -- varchar2가 varchar
    title VARCHAR(200) NOT NULL,
    content TEXT,
    view_cnt INT(10) DEFAULT 0,
    reg_date DATETIME DEFAULT current_timestamp, --date가 datetime / 시쓰데이트가 current-timestamp
    CONSTRAINT pk_tbl_board PRIMARY KEY (board_no)
);

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

