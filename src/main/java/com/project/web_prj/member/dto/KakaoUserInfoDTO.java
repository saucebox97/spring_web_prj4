package com.project.web_prj.member.dto;

import lombok.*;

@Setter @Getter @ToString
@NoArgsConstructor
@AllArgsConstructor
public class KakaoUserInfoDTO {

    private String nickName;
    private String profileImg;
    private String email;
    private String gender;
}
