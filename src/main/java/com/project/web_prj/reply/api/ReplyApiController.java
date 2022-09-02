package com.project.web_prj.reply.api;

import com.project.web_prj.common.paging.Page;
import com.project.web_prj.reply.domain.Reply;
import com.project.web_prj.reply.service.ReplyService;
import com.project.web_prj.util.LoginUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Map;

@RestController //api
@RequiredArgsConstructor // 의존성
@Log4j2
@RequestMapping("/api/v1/replies") // 공통 api / 비동기로 실시간으로뺴와서 화면에 나타나지않음
@CrossOrigin // 외부접근을 할 수 있게해줌
public class ReplyApiController {

    private final ReplyService replyService;

    /*
        - 댓글 목록 조회요청 : /api/v1/replies - GET
        - 댓글 개별 조회요청 : /api/v1/replies/72 - GET /(repliesNumber)를붙임 마지막에
        - 댓글 쓰기 요청 : /api/v1/replies - POST
        - 댓글 수정 요청 : /api/v1/replies/72 - PUT / (repliesNumber)를붙임 마지막에
        - 댓글 삭제 요청 : /api/v1/replies/72 - DELETE / (repliesNumber)를붙임 마지막에
     */

    // 댓글 목록 요청
    @GetMapping("")
    public Map<String, Object> list(Long boardNo, Page page) {
        log.info("34 /api/v1/replies GET! bno={}, page={}", boardNo, page);

        Map<String, Object> replies = replyService.getList(boardNo, page);

        return replies;
    }

    // 댓글 등록 요청
    @PostMapping("") // @RequestBody 제이슨으로 클라이언트가서버에보냄
    public String create(@RequestBody Reply reply, HttpSession session) {
        reply.setAccount(LoginUtils.getCurrentMemberAccount(session));
        log.info("45 /api/v1/replies POST! - {}", reply);
        boolean flag = replyService.write(reply);
//        replyService.write(reply); 한번등록할떄 1개가 더등록됌
        return flag ? "insert-success" : "insert-fail";
    }

    // 댓글 수정 요청
    @PutMapping("/{rno}") // replyNo은 주소창에넣는다
    public String modify(@PathVariable Long rno, @RequestBody Reply reply) {
        reply.setReplyNo(rno); // reply에는 번호가없으니 넣어줘야됀다
        log.info("/api/v1/replies POST! - {}", reply);
        boolean flag = replyService.modify(reply);
        return flag ? "mod-success" : "mod-fail";
    }

    // 댓글 삭제
    @DeleteMapping("/{rno}")
    public String delete(@PathVariable Long rno) {

        log.info("/api/v1/replies POST! - {}", rno);
        boolean flag = replyService.remove(rno);
        return flag ? "del-success" : "del-fail";
    }
}
