package com.example.querydsl.repository.practice;

import com.example.querydsl.dto.MemberDto;
import com.example.querydsl.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class TestController {

    private final TestRepository testRepository;

    @GetMapping("/test/search")
    public List<MemberDto> search() {
        ArrayList<MemberDto> members = new ArrayList<>();

        List<Member> all = testRepository.findAll();
        for (Member member : all) {
            MemberDto memberDto = new MemberDto(member);
            members.add(memberDto);
        }
        return members;
    }

    @GetMapping("/test2/search")
    public List<MemberDto> searchMemberDto(@RequestParam String usernameParam, @RequestParam Integer ageParam) {
        return testRepository.searchMember(usernameParam, ageParam);
    }
}
