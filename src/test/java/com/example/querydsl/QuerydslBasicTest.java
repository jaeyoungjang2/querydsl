package com.example.querydsl;

import com.example.querydsl.entity.Member;
import com.example.querydsl.entity.QMember;
import com.example.querydsl.entity.Team;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import static com.example.querydsl.entity.QMember.*;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
public class QuerydslBasicTest {

    @Autowired
    EntityManager em;


    JPAQueryFactory queryFactory;

    @BeforeEach
    public void before() {
        // jpaqueryfactory를 만들 때 entityManager를 넘겨줌
        // entitymanager가 데이터를 찾음
        // multi thread 동시성 문제를 고민할 필요없음
        queryFactory = new JPAQueryFactory(em);

        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        em.persist(teamA);
        em.persist(teamB);

        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 10, teamA);
        Member member3 = new Member("member3", 10, teamA);
        Member member4 = new Member("member4", 10, teamA);


        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);
    }

    @Test
    public void startJPQL() {
        // member1을 찾아라.
        String qlString =
                "select m from Member m " +
                "where m.username = :username";

        Member findMember = em.createQuery(qlString, Member.class)
                .setParameter("username", "member1")
                .getSingleResult();

        assertThat(findMember.getUsername()).isEqualTo("member1");
    }

    @Test
    public void startQuerydsl() {
        // 별칭 'm'을 줌, 크게 중요한건 아님
//        QMember m = new QMember("m");
        QMember m = member;


        Member findMember = queryFactory
                .select(m)
                .from(m)
                // jdbc에 있는 prepare statement로 자동으로 parameter biniding을 함
                // 이렇게 하면 sql injection 공격으로 부터 방어 가능?
                // 컴파일 시점에서 오류를 잡아줄 수 있음 (jpql에서 문자열로 query를 작성하지 않기 때문에)
                .where(m.username.eq("member1"))
                .fetchOne();

        assertThat(findMember.getUsername()).isEqualTo("member1");
    }

    @Test
    public void search() {
        // username == 1 && age == 10
        Member findMember = queryFactory
                .selectFrom(member)
                .where(member.username.eq("memer1")
                        .and(member.age.eq(10)))
                .fetchOne();

        assertThat(findMember.getUsername()).isEqualTo("member1");
    }
}
