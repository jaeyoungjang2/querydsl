package com.example.querydsl.controller;

import com.example.querydsl.entity.Member;
import lombok.Data;
import org.springframework.data.domain.Page;

@Data
public class MemberTestDto {

    private String username;
    private Integer age;


    public MemberTestDto(Member member) {
        this.username = member.getUsername();
        this.age = member.getAge();

    }
}
