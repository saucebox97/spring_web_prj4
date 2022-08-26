package com.project.web_prj.board.controller;

import com.project.web_prj.board.domain.Board;
import com.project.web_prj.board.service.BoardService;
import com.project.web_prj.common.paging.Page;
import com.project.web_prj.common.paging.PageMaker;
import com.project.web_prj.common.search.Search;
import com.project.web_prj.util.FileUtils;
import com.project.web_prj.util.LoginUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 게시물 목록요청: /board/list: GET
 * 게시물 상세조회요청: /board/content: GET
 * 게시글 쓰기 화면요청: /board/write: GET
 * 게시글 등록요청: /board/write: POST
 * 게시글 삭제요청: /board/delete: GET
 * 게시글 수정화면요청: /board/modify: GET
 * 게시글 수정요청: /board/modify: POST
 */


@Controller
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {

    private final BoardService boardService;

    private static final String UPLOAD_PATH = "E:\\sl_test\\upload";

    // 게시물 목록 요청
    @GetMapping("/list")
    public String list(@ModelAttribute("s") Search search, Model model) {
        log.info("controller request /board/list GET! - search: {}", search);

        Map<String, Object> boardMap = boardService.findAllService(search);
        log.debug("return data - {}", boardMap);

        // 페이지 정보 생성
        PageMaker pm = new PageMaker(
                new Page(search.getPageNum(), search.getAmount())
                , (Integer) boardMap.get("tc"));

        model.addAttribute("bList", boardMap.get("bList"));
        model.addAttribute("pm", pm);

        return "board/board-list";
    }

    // 게시물 상세 조회 요청
    @GetMapping("/content/{boardNo}")
    public String content(@PathVariable Long boardNo
            , Model model, HttpServletResponse response, HttpServletRequest request
            , @ModelAttribute("p") Page page)
    {
        log.info("controller request /board/content GET! - {}", boardNo);
        Board board = boardService.findOneService(boardNo, response, request);
        log.info("return data - {}", board);
        model.addAttribute("b", board);
//        model.addAttribute("p", page); @ModelAttribute쓸거면 안써도됌
        return "board/board-detail";
    }

    // 게시물 쓰기 화면 요청
    @GetMapping("/write")
    public String write(HttpSession session, RedirectAttributes ra) {

//        if (session.getAttribute("loginUser") == null) {
//            ra.addFlashAttribute("warningMsg", "forbidden");
//            return "redirect:/member/sign-in";
//        }

        log.info("controller request /board/write GET!");
        return "board/board-write";
    }

    // 게시물 등록 요청
    @PostMapping("/write") // board-write72줄(업로드누르면) ,77줄(작성하기누르면)에서 files라는이름으로옴
    public String write(Board board,
                        @RequestParam("files") List<MultipartFile> fileList,
                        RedirectAttributes ra,
                        HttpSession session) {
        log.info("101 controller request /board/write POST! - {}", board);

        log.info("103 /upload POST! - {}", fileList); // 여기에 파일이들어오는지

        for (MultipartFile file: fileList) {
            log.info("file-name: {}", file.getName());
            log.info("file-origin-name: {}", file.getOriginalFilename());
            log.info("file-size: {}KB", (double) file.getSize() / 1024);
            log.info("file-type: {}", file.getContentType());
            System.out.println("=============================================");

            // 서버에 업로드파일 저장

            // 1. 세이브파일 객체 생성
            // -  첫번째 파라미터는 파일 저장경로 지정, 두번째 파일명지정하기위해쓰는 코드
//        File f = new File(uploadPath, file.getOriginalFilename());
//
//        try {
//            file.transferTo(f); // 파일업로드폴더에저장 코드
//        } catch (IOException e) {
//            e.printStackTrace(); // 예외 결과를 화면에 출력함
//        }

            FileUtils.uploadFile(file, UPLOAD_PATH);
        }

//        if (fileList != null) {
//            List<String> fileNames = new ArrayList<>();
//            for (MultipartFile f : fileList) {
//                log.info("attachmented file-name: {}", f.getOriginalFilename());
//                fileNames.add(f.getOriginalFilename());
//            }
//            // board객체에 파일명 추가
//            board.setFileNames(fileNames);
//        }

        // 현재 로그인 사용자 계정명 추가
        board.setAccount(LoginUtils.getCurrentMemberAccount(session));

        boolean flag = boardService.saveService(board);
        // 모델은 포워딩할때 리다이렉트할떄는 RedirectAttributes
        // 게시물 등록에 성공하면 클라이언트에 성공메시지를 전송/리퀘스트에받은거이기때문에 리다이렉트하면 사라짐
        if (flag) ra.addFlashAttribute("msg", "reg-success");

        return flag ? "redirect:/board/list" : "redirect:/";
    }

    // 게시물 삭제 확인 요청
    @GetMapping("/delete")
    public String delete(@ModelAttribute("boardNo") Long boardNo, Model model) {

        log.info("controller request /board/delete GET! - bno: {}", boardNo);

        model.addAttribute("validate", boardService.getMember(boardNo));

        return "board/process-delete";
    }

    // 게시물 삭제 확정 요청
    @PostMapping("/delete")
    public String delete(Long boardNo) {
        log.info("controller request /board/delete POST! - bno: {}", boardNo);

        return boardService.removeService(boardNo) ? "redirect:/board/list" : "redirect:/";
    }

    // 수정 화면 요청
    @GetMapping("/modify")
    public String modify(Long boardNo, Model model, HttpServletRequest request, HttpServletResponse response) {
        log.info("controller request /board/modify GET! - bno: {}", boardNo);
        Board board = boardService.findOneService(boardNo, response, request);
        log.info("find article: {}", board);

        model.addAttribute("board", board);
        model.addAttribute("validate", boardService.getMember(boardNo));

        return "board/board-modify";
    }

    // 수정 처리 요청
    @PostMapping("/modify")
    public String modify(Board board) {
        log.info("controller request /board/modify POST! - {}", board);
        boolean flag = boardService.modifyService(board);
        return flag ? "redirect:/board/content/" + board.getBoardNo() : "redirect:/";
    }

    // 특정 게시물에 붙은 첨부파일경로 리스트를 클라이언트에게 비동기 전송
    @GetMapping("/file/{bno}")
    @ResponseBody
    public ResponseEntity<List<String>> getFiles(@PathVariable Long bno) {

        List<String> files = boardService.getFiles(bno);
        log.info("/board/file/{} GET! ASYNC - {}", bno, files);

        return new ResponseEntity<>(files, HttpStatus.OK);
    }

}