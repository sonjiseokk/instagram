package com.meta.instagram.domain.entity.repository.querydsl;

import com.meta.instagram.domain.entity.Post;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.meta.instagram.domain.entity.QPost.post;
import static com.meta.instagram.domain.entity.QPostTag.postTag;


@Repository
@RequiredArgsConstructor
public class PostRepositoryQuery{
    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    /**
     * 게시물을 조회할때 연관된 태그들(s)을 같이 반환하는 기능
     * @return post
     * @throws IllegalArgumentException
     */
    public Post findById(Long id) {
        Post findPost = queryFactory.query()
                .select(post)
                .from(post)
                .leftJoin(post.postTags, postTag)
                .where(post.id.eq(id))
                .fetchOne();

        if (findPost == null) {
            throw new IllegalArgumentException("잘못된 ID 입니다.");
        }
        return findPost;
    }
}
