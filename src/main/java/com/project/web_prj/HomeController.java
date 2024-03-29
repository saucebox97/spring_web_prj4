package com.project.web_prj;

import com.project.web_prj.member.domain.Member;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Controller
@Log4j2
public class HomeController {

    @GetMapping("/")
    public String home(Model model, HttpSession session) {
        log.info("welcome page open!");
//        model.addAttribute("scroll", true);

//        Member loginUser = (Member) session.getAttribute("loginUser"); // 세션을 뒤져봐서 로그인확인을 해본다
//        if (loginUser == null) {
//            model.addAttribute("login", false);
//        } else {
//            model.addAttribute("login", true);
//        }

        return "/index";
    }

    @GetMapping("/haha") // 서버가 클라이언트한테 줄떄슨다
    @ResponseBody // 리턴 데이터가 뷰포워딩이 아닌 JSON으로 전달됨
    public Map<String, String> haha() {
        Map<String, String> map = new HashMap<>();
        map.put("a", "aaa");
        map.put("b", "bbb");
        map.put("c", "ccc");
        return map;
    }

}
