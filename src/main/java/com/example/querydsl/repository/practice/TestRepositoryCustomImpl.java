package com.example.querydsl.repository.practice;

import com.example.querydsl.dto.MemberDto;
import com.example.querydsl.dto.QMemberDto;
import com.example.querydsl.entity.Member;
import com.example.querydsl.entity.QMember;
import com.querydsl.core.QueryFactory;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import java.util.List;

import static com.example.querydsl.entity.QMember.*;


@Repository
public class TestRepositoryCustomImpl implements TestRepositoryCustom {

    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    public TestRepositoryCustomImpl(EntityManager em) {
        this.em = em;
        this.queryFactory = new JPAQueryFactory(em);

    }


    @Override
    public List<MemberDto> searchMember(String usernameParam, Integer ageParam) {
        return queryFactory
                .select(new QMemberDto(member.username, member.age))
                .from(member)
                .where(
                        usernameEq(usernameParam),
                        ageEq(ageParam))
                .fetch();
    }

    private Predicate usernameEq(String usernameParam) {
        return StringUtils.hasText(usernameParam) ? member.username.eq(usernameParam) : null;
    }
    private Predicate ageEq(Integer ageParam) {
        return ageParam != null ? member.age.eq(ageParam) : null;
    }
}
