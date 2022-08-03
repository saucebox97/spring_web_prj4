package com.project.web_prj.member.service;

import com.project.web_prj.member.domain.Member;
import com.project.web_prj.member.dto.LoginDTO;
import com.project.web_prj.member.repository.MemberMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

import static com.project.web_prj.member.service.LoginFlag.*;

@Service
@Log4j2
@RequiredArgsConstructor
public class MemberService {

    private final MemberMapper memberMapper;
    private final BCryptPasswordEncoder encoder;

    // 회원 가입 중간 처리
    public boolean signUp(Member member) {
        // 비밀번호 인코딩
        member.setPassword(encoder.encode(member.getPassword()));

        return memberMapper.register(member);
    }

    // 중복확인 중간처리

    /**
     * 계정과 이메일의 중복을 확인하는 메서드
     * @param type - 확인할 정보 (ex: account or email)
     * @param value = 확인할 값
     * @return 중복이라면 true, 중복이 아니라면 false
     */
    public boolean checkSignUpValue(String type, String value) {
        Map<String, Object> checkMap = new HashMap<>();
        checkMap.put("type", type);
        checkMap.put("value", value);

        return memberMapper.isDuplicate(checkMap) == 1;
    }

    // 회원 정보 조회 중간 처리
    public Member getMember(String account) {
        return memberMapper.findUser(account);
    }

    // 로그인 처리
    public LoginFlag login(LoginDTO inputData, HttpSession session) {
        // 회원가입 여부 확인 / findUser = 회원정보조회
        Member foundMember = memberMapper.findUser(inputData.getAccount());
        if (foundMember != null) {
            if (encoder.matches(inputData.getPassword(), foundMember.getPassword())) {
                // 로그인 성공
                // 세션에 사용자 정보기록 저장
                session.setAttribute("loginUser", foundMember);
                session.setMaxInactiveInterval(60 * 60); // 1시간
                return SUCCESS;
            } else {
                // 비번 틀림
                return NO_PW;
            }
        } else {
            // 아이디 없음
            return NO_ACC;
        }
    }

}
