package com.project.web_prj;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Log4j2
public class HomeController {

    @GetMapping("/")
    public String home(Model model) {
        log.info("welcome page open!");
        model.addAttribute("scroll", true);
        return "/index";
    }
}
