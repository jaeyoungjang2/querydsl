package com.example.querydsl.repository.practice;


import com.example.querydsl.dto.MemberDto;
import com.example.querydsl.entity.Member;

import java.util.List;

public interface TestRepositoryCustom {
    List<MemberDto> searchMember(String usernameParam, Integer ageParam);
}
