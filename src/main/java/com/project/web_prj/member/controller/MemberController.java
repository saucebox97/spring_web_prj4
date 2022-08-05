package com.project.web_prj.member.controller;

import com.project.web_prj.member.domain.Member;
import com.project.web_prj.member.domain.SNSLogin;
import com.project.web_prj.member.dto.LoginDTO;
import com.project.web_prj.member.service.KakaoService;
import com.project.web_prj.member.service.LoginFlag;
import com.project.web_prj.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static com.project.web_prj.member.domain.OAuthValue.KAKAO_APP_KEY;
import static com.project.web_prj.member.domain.OAuthValue.KAKAO_REDIRECT_URI;
import static com.project.web_prj.util.LoginUtils.*;

@Controller
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;
    private final KakaoService kakaoService;

    // 회원가입 양식 띄우기 요청
    @GetMapping("/sign-up")
    public void signUp() { // 요청 url이랑 포워딩할 경로랑 똑같을 경우 void처리가능
        log.info("/member/sign-up GET! - forwarding to sign-up.jsp");
    }

    // 회원가입 처리 요청
    @PostMapping("/sign-up")
    public String signUp(Member member, RedirectAttributes ra) {
        log.info("/member/sign-up POST ! - {}", member);
        boolean flag = memberService.signUp(member);
        // 성공하면 축하메세지
        ra.addFlashAttribute("msg", "reg-success");
        return flag ? "redirect:/member/sign-in" : "redirect:/member/sign-up";
    }

    // 아이디, 이메일 중복확인 비동기 요청 처리
    @GetMapping("/check")
    @ResponseBody // 비동기니까
    public ResponseEntity<Boolean> check(String type, String value) {
        log.info("/member/check?type={}$value={} GET!! ASYNC", type, value);
        boolean flag = memberService.checkSignUpValue(type, value);

        return new ResponseEntity<>(flag, HttpStatus.OK);
    }

    // 로그인 화면을 열어주는 요청처리
    @GetMapping("/sign-in") // url이랑 파일경로가똑같아서 void로해도됌 /boardinterceptor에서 로그인안됏을떄 message에 no-login이 담김
    public void signIn(@ModelAttribute("message") String message, HttpServletRequest request, Model model) {
        log.info("/member/sign-in GET! - forwarding to sign-in.jsp");

        // 요청 정보 헤더 안에는 Referer라는 키가 있는데
        // 여기 안에는 이페이지를 진입할 떄 어디에서 탔는지 URI정보가 돌아간다
        String referer = request.getHeader("Referer");
        log.info("referer: {}", referer);

        request.getSession().setAttribute("redirectURI", referer);

        model.addAttribute("kakaoAppKey", KAKAO_APP_KEY);
        model.addAttribute("kakaoRedirect", KAKAO_REDIRECT_URI);
    }

    // 로그인 요청 처리 sign-in 29줄에서 아디/비번/자동로그인이옴
    @PostMapping("/sign-in")
    public String signIn(LoginDTO inputData
            , Model model
            , HttpSession session // 세션정보 객체
            , HttpServletResponse response
    ) {

        log.info("/member/sign-in POST - {}", inputData);
//        log.info("session timeout : {}", session.getMaxInactiveInterval());/기본값 30분

        // 로그인 서비스 호출
        LoginFlag flag = memberService.login(inputData, session, response);

        if (flag == LoginFlag.SUCCESS) {
            log.info("login success!!");
            String redirectURI = (String) session.getAttribute("redirectURI"); // 61줄 어디서 진입했는지
            return "redirect:" + redirectURI; // 로그인후 페이지
        }
        model.addAttribute("loginMsg", flag);
        return "member/sign-in";

    }

    @GetMapping("/sign-out")
    public String signOut(HttpServletRequest request, HttpServletResponse response) throws Exception{

        HttpSession session = request.getSession();

        if (isLogin(session)) {

            // 만약 자동로그인 상태라면 해체한다.
            if (hasAutoLoginCookie(request)) {
                memberService.autoLogout(getCurrentMemberAccount(session), request, response);
            }

            // SNS 로그인 상태라면 해당 SNS 로그아웃 처리를 진행한다
            SNSLogin from = (SNSLogin) session.getAttribute(LOGIN_FROM);
            switch (from) {
                case KAKAO:
                    kakaoService.logout((String) session.getAttribute("accessToken"));
                    break;
                case NAVER:
                    break;
                case GOOGLE:
                    break;
                case FACEBOOK:
                    break;
            }

            // 1. 세션에서 정보를 삭제한다.
            session.removeAttribute(LOGIN_FLAG);

            // 2. 세션을 무료화한다.
            session.invalidate();
            return "redirect:/";
        }
        return "redirect:/member/sign-in";
    }

}
