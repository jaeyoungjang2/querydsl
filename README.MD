## 1. Querydsl 설정과 검증


- q 파일 생성하기
```
./gradlew compileQuerydsl
gradle -> other -> compileQuerydsl
```

> q파일 생성된 결과는 아래 위치에서 확인할 수 있습니다
![img.png](img.png)

<br/><br/>

---
## 엔티티 생성
![img_1.png](img_1.png)


---
## Q-type 활용
  - 별칭을 직접 입력하는 방법<br/>
```- new QMember("m");```
  - 기본 인스턴스 사용 
  - 같은 테이블을 조인하는 경우를 제외하면 기본 인스턴스를 사용한다.<br/>
```- QMember.member```

querydsl에서 query와 관련된 것은 q타입을 이용합니다.
querydsl은 jpql의 builder 역할을 하는 것입니다.
결국은 jpql로 변하는 것입니다.


<br/><br/>

---
## 정적 쿼리를 이용해서 and 조건을 사용하는 두가지 방법
  - chain을 이용하는 방법
  - ","를 이용하여 연결하는 방법 (null 처리 용이, 동적 쿼리를 만들 때도 사용)

![img_2.png](img_2.png)


<br/><br/>

---
## 결과 조회를 하는 다양한 방법

![img_3.png](img_3.png)



<br/><br/>

---
## 정렬
 - 아래와 같이 편하게 정렬할 수 있다.
 - 동적 정렬은 아니라는거..

![img_4.png](img_4.png)

<br/><br/>

---
## 페이징
 - offset limit을 이용한 paging 활용 방법

![img_5.png](img_5.png)

<br/><br/>

---
## 집합
 - group by, having 에 관한 함수이다.

![img_6.png](img_6.png)


<br/><br/>

---
## 기본 조인
- join(조인 대상, 별칭으로 사용할 Q-type)

![img_7.png](img_7.png)


<br/><br/>

---
## 세타 조인
 - 외부 조인은 기본적으로 불가능 하나, on을 사용하면 외부 조인이 가능하다.

![img_8.png](img_8.png)


<br/><br/>

---
## On절을 활용한 조인
 - 조인 대상 필터링
 - 연관관계 없는 엔티티 외부 조인
 - innerjoin인 경우는 where이나 on이나 차이가 없다. (leftjoin, rightjoin일 경우만 on절을 사용)

### [ 조인 대상 필터링 ]
![img_9.png](img_9.png)

- 아래와 같은 결과를 얻을 수 있습니다.

![img_10.png](img_10.png)

### [ 연관관계 없는 엔티티 외부 조인 ]
 - leftjoin이 일반적인 구조와 다른 것을 살펴볼 수 있다.
 - 일반적인 구조는 leftJoin(member.team, team)

![img_12.png](img_12.png)
 - member의 이름과 team의 이름이 같을 때만 team의 값을 가져온 결과

![img_11.png](img_11.png)


<br/><br/>

---
## 페치 조인
 - SQL에서 제공하는 기능이 아님
 - 한방에 가져오기 위함
 - 성능 최적화를 위함


