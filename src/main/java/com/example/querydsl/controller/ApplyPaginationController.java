package com.example.querydsl.controller;

import com.example.querydsl.dto.MemberDto;
import com.example.querydsl.dto.MemberSearchCondition;
import com.example.querydsl.entity.Member;
import com.example.querydsl.repository.support.MemberTestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
@RequiredArgsConstructor
public class ApplyPaginationController {

    private final MemberTestRepository memberTestRepository;

    @GetMapping("apply/v1/members")
    public ArrayList<MemberTestDto> serachV1Members(MemberSearchCondition condition, Pageable pageable) {
        Page<Member> members = memberTestRepository.applyPagination2(condition, pageable);
        ArrayList<MemberTestDto> list = new ArrayList<>();
        for (Member member : members) {
            list.add(new MemberTestDto(member));
        }
        return list;
    }

}
