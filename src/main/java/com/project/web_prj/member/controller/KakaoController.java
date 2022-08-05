package com.project.web_prj.member.controller;

import com.project.web_prj.member.domain.Member;
import com.project.web_prj.member.domain.OAuthValue;
import com.project.web_prj.member.dto.KakaoUserInfoDTO;
import com.project.web_prj.member.service.KakaoService;
import com.project.web_prj.util.LoginUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

import static com.project.web_prj.member.domain.OAuthValue.KAKAO_APP_KEY;
import static com.project.web_prj.member.domain.OAuthValue.KAKAO_REDIRECT_URI;
import static com.project.web_prj.member.domain.SNSLogin.KAKAO;
import static com.project.web_prj.util.LoginUtils.LOGIN_FLAG;
import static com.project.web_prj.util.LoginUtils.LOGIN_FROM;

@Controller
@Log4j2
@RequiredArgsConstructor // 생성자 주입
public class KakaoController {

    private final KakaoService kakaoService;

    @GetMapping("/kakao-test")
    public void kakaoTest(Model model) {
        log.info("foward to kakao-test.jsp!");
        model.addAttribute("appKey", KAKAO_APP_KEY);
        model.addAttribute("redirectUri", KAKAO_REDIRECT_URI);
    }

    // 카카오 인증서버가 보내준 인가코드를 받아서 처리할 메서드
    @GetMapping(KAKAO_REDIRECT_URI)
    public String kakaoLogin(String code, HttpSession session) throws Exception {
        log.info("{} GET!! code - {}", KAKAO_REDIRECT_URI, code); // code가 인가코드 인가코드받기

        // 인가코드를 통해 액체스토큰 발급받기
        // 우리서버에서 카카오서버로 통신을 해야할.
        String accessToken = kakaoService.getAccessToken(code);

        // 액세스 토큰을 통해 사용자 정보 요청(프로필사진, 닉네임 등)
        KakaoUserInfoDTO userInfo = kakaoService.getKakaoUserInfo(accessToken);

        // 로그인 처리
        if (userInfo != null) {
            Member member = new Member();
            member.setAccount(userInfo.getEmail());
            member.setName(userInfo.getNickName());
            member.setEmail(userInfo.getEmail());
            session.setAttribute(LOGIN_FLAG, member);
            session.setAttribute("profile_path", userInfo.getProfileImg()); // kakao-test.jsp 43줄에 넣을려고
            session.setAttribute(LOGIN_FROM, KAKAO);
            session.setAttribute("accessToekn", accessToken); // 로그아웃에넣어줄토큰
            return "redirect:/kakao-test";

        }

        return "redirect:/member/sign-in";
    }

    // 카카오 로그아웃
    @GetMapping("/kakao/logout")
    public String kakaoLogout(HttpSession session) throws Exception {

        // 카카오 로그아웃 처리
        kakaoService.logout((String) session.getAttribute("accessToken"));

        // 우리 서비스 로그아웃
        session.invalidate();

        return "redirect:/kakao-test";
    }
}
