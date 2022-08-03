package com.project.web_prj.member.domain;

import lombok.*;

import java.util.Date;
// @Date쓰면안됌 @ToString을 빼야될떄가있음 /@Date는 전부다만들어줌
@Getter @Setter @ToString
@NoArgsConstructor
@AllArgsConstructor
public class Member {

    private String account;
    private String password;
    private String name;
    private String email;
    private Auth auth;
    private Date regDate;
}
