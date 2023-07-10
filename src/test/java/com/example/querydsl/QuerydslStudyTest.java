package com.example.querydsl;

import com.example.querydsl.dto.MemberDto;
import com.example.querydsl.dto.QMemberDto;
import com.example.querydsl.entity.Member;
import com.example.querydsl.entity.QMember;
import com.example.querydsl.entity.QTeam;
import com.example.querydsl.entity.Team;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.transaction.Transactional;

import java.util.List;

import static com.example.querydsl.entity.QMember.*;
import static com.example.querydsl.entity.QTeam.*;
import static org.assertj.core.api.Assertions.*;


@SpringBootTest
@Transactional
public class QuerydslStudyTest {


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
        Member member2 = new Member("member2", 20, teamA);
        Member member3 = new Member("member3", 30, teamB);
        Member member4 = new Member("member4", 40, teamB);


        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);
    }

//    where절 and 조건을 넣는 두가지 방법
//    예시 쿼리) 사람이름은 member1, 나이는 10살
//    1. chain 형식
    @Test
    public void chainTest() {
        Member member1 = queryFactory
                .selectFrom(member)
                .where(member.username.eq("member1")
                        .and(member.age.eq(10)))
                .fetchOne();

        assertThat(member1.getUsername()).isEqualTo("member1");
        assertThat(member1.getAge()).isEqualTo(10);
    }

//    2. ","을 이용하는 방식
    @Test
    public void paramTest() {
        Member member1 = queryFactory
                .selectFrom(member)
                .where(member.username.eq("member1")
                        , member.age.eq(10))
                .fetchOne();

        assertThat(member1.getUsername()).isEqualTo("member1");
        assertThat(member1.getAge()).isEqualTo(10);
    }



//    페이징
    @Test
    public void phasingTest() {
        List<Member> result = queryFactory
                .selectFrom(member)
                .offset(0)
                .limit(2)
                .fetch();

        assertThat(result.size()).isEqualTo(2);
    }

//    연관 관계가 없는 세타 조인
//    에시 쿼리) 사람 이름과 팀 이름이 같은 사람 조회
    @Test
    public void thetaJoin() {

        em.persist(new Member("teamA"));

        List<Member> result = queryFactory
                .select(member)
                .from(member, team)
                .where(member.username.eq(team.name))
                .fetch();

        assertThat(result)
                .extracting("username")
                .containsExactly("teamA");
    }



    @PersistenceUnit
    EntityManagerFactory emf;

//    페치조인
//    member의 이름은 "member1"
    @Test
    public void fetchJoin() {
        em.flush();
        em.clear();

        Member member1 = queryFactory
                .selectFrom(member)
                .join(member.team, team).fetchJoin()
                .where(member.username.eq("member1"))
                .fetchOne();

        boolean loaded = emf.getPersistenceUnitUtil().isLoaded(member1.getTeam());
        assertThat(loaded).isTrue();
    }

//    where절 서브쿼리 (JPAExpression)
//    where: memeber age >= (subQuery: member의 평균 나이)
    @Test
    public void subQueryTest() {
        QMember subMember = new QMember("m");


        List<Member> result = queryFactory
                .selectFrom(member)
                .where(member.age.goe(
                        JPAExpressions
                                .select(subMember.age.avg())
                                .from(subMember)))
                .fetch();
        // 평균나이 25살
        // 30, 40
        assertThat(result).extracting("age").containsExactly(30, 40);
    }


//    프로젝션
//    프로젝션이 한개인 경우
//    예시 쿼리) select member.username
    @Test
    public void oneProjection() {
        List<String> result = queryFactory
                .select(member.username)
                .from(member)
                .fetch();

        assertThat(result).containsExactly("member1", "member2", "member3", "member4");
    }

    //    프로젝션 대상이 둘 이상인 경우 (tuple)
//    예시 쿼리) select member.username, member.age
    @Test
    public void twoProjections() {
        List<Tuple> result = queryFactory
                .select(member.username, member.age)
                .from(member)
                .fetch();

        for (Tuple tuple : result) {
            String username = tuple.get(member.username);
            Integer age = tuple.get(member.age);
            System.out.println("username = " + username);
            System.out.println("age = " + age);
        }
    }


//    프로젝션 대상을 DTO로 (MemberDto) 받아오기 (Projections 사용, setter, 필드, 생성자)
//    예시 쿼리) select member.username, member.age
    @Test
    public void setterProjection() {
        List<MemberDto> result = queryFactory
                .select(Projections.bean(MemberDto.class,
                        member.username,
                        member.age))
                .from(member)
                .fetch();

        for (MemberDto memberDto : result) {
            System.out.println("memberDto = " + memberDto);
        }
    }

    @Test
    public void fieldProjection() {
        List<MemberDto> result = queryFactory
                .select(Projections.fields(MemberDto.class,
                        member.username,
                        member.age))
                .from(member)
                .fetch();

        for (MemberDto memberDto : result) {
            System.out.println("memberDto = " + memberDto);
        }
    }

    @Test
    public void constructorProjection() {
        List<MemberDto> result = queryFactory
                .select(Projections.constructor(MemberDto.class,
                        member.username,
                        member.age))
                .from(member)
                .fetch();

        for (MemberDto memberDto : result) {
            System.out.println("memberDto = " + memberDto);
        }
    }


//    QueryProjection
    @Test
    public void queryProjectionAnnotationTest() {
        List<MemberDto> result = queryFactory
                .select(new QMemberDto(member.username, member.age))
                .from(member)
                .fetch();

        for (MemberDto memberDto : result) {
            System.out.println("memberDto = " + memberDto);
        }
    }

//    동적쿼리
//    BooleanBuilder 사용
    @Test
    public void booleanBuilder() {
        String usernameParam = "member1";
        Integer ageParam = 10;

        BooleanBuilder builder = new BooleanBuilder();
        if (!StringUtils.hasText(usernameParam)) {
            builder.and(member.username.eq(usernameParam));
        }
        if (ageParam != null) {
            builder.and(member.age.eq(ageParam));
        }

        Member member1 = queryFactory
                .selectFrom(member)
                .where(builder)
                .fetchOne();

        System.out.println("member1 = " + member1);
    }


//    where 다중 파라미터
    @Test
    public void QuerydslStudyTest() {
        String usernameParam = "member1";
        Integer ageParam = 10;

        Member member1 = queryFactory
                .selectFrom(member)
                .where(usernameEq(usernameParam),
                        ageEq(ageParam))
                .fetchOne();

        System.out.println("member1 = " + member1);
    }

    private Predicate usernameEq(String usernameParam) {
        return !StringUtils.hasText(usernameParam) ? member.username.eq(usernameParam) : null;
    }
    private Predicate ageEq(Integer ageParam) {
        return ageParam != null ? member.age.eq(ageParam) : null;
    }

//    JPA Repository와 Querydsl


}
