package com.example.querydsl.repository.practice;

import com.example.querydsl.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestRepository extends JpaRepository<Member, Integer>, TestRepositoryCustom {

}
