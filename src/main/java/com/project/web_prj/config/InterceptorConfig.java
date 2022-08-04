package com.project.web_prj.config;

import com.project.web_prj.interceptor.AfterLoginInterceptor;
import com.project.web_prj.interceptor.AutoLoginInterceptor;
import com.project.web_prj.interceptor.BoardInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// 다양한 인터셉터들을 관리하는 설정 클래스
@Configuration
@RequiredArgsConstructor // 생성자주입
public class InterceptorConfig implements WebMvcConfigurer {

    private final BoardInterceptor boardInterceptor;
    private final AfterLoginInterceptor afterLoginInterceptor;
    private final AutoLoginInterceptor autoLoginInterceptor;

    // 인터셉터 설정 추가 메서드
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //게시판 인터셉터 설정
        registry.addInterceptor(boardInterceptor) // 인터셉터추가 /로그인햇는지 검사하는 코드
                .addPathPatterns("/board/*") // 경로에 인터셉터추가
                .excludePathPatterns("/board/list", "/board/content"); // 이 경로는

        // 애프터 로그인 인터셉터 설정
        registry.addInterceptor(afterLoginInterceptor)
                .addPathPatterns("/member/sign-in", "/member/sign-up"); // 로그인후 이페이지에 못들어감

        // 자동 로그인 인터셉터 설정
        registry.addInterceptor(autoLoginInterceptor)
                .addPathPatterns("/**");
    }
}
