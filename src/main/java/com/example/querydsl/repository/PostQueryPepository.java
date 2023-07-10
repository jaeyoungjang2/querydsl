//package com.example.querydsl.repository;
//
//import com.querydsl.jpa.impl.JPAQueryFactory;
//import lombok.RequiredArgsConstructor;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.domain.SliceImpl;
//import org.springframework.stereotype.Repository;
//
//@RequiredArgsConstructor
//@Repository
//public class PostQueryPepository {
//
//	private final JPAQueryFactory queryFactory;
//
//
//
//	public Slice<PostResponse> findPostsWithNoOffset(Long lastPostId, Pageable pageable) {
//
//		List<Post> fetch = queryFactory.selectFrom(post)
//										.where(getWhereLastPostIdLowerThan(lastPostId))
//										.orderBy(post.id.desc())
//										.limit(pageable.getPageSize() + 1)
//										.fetch();
//
//		List<PostResponse> content = fetch.stream()
//										.map(p -> new PostResponse(p))
//										.collect(Collectors.toList());
//
//		boolean hasNext = false;
//		if (content.size() > pageable.getPageSize()) {
//			content.remove(pageable.getPageSize());
//			hasNext = true;
//		}
//
//		return new SliceImpl<>(content, pageable, hasNext);
//
//	}
//}