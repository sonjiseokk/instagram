package com.meta.instagram.domain.entity.repository.querydsl;

import com.meta.instagram.domain.dto.PostResponse;
import com.meta.instagram.domain.dto.SearchCondition;
import com.meta.instagram.domain.entity.Post;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import static com.meta.instagram.domain.entity.QAccount.account;
import static com.meta.instagram.domain.entity.QPost.post;
import static com.meta.instagram.domain.entity.QPostComment.postComment;
import static com.meta.instagram.domain.entity.QPostLike.postLike;
import static com.meta.instagram.domain.entity.QPostTag.postTag;


@Repository
@RequiredArgsConstructor
public class PostRepositoryQuery{
    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    /**
     * 단건 게시물을 조회할때 연관된 태그들(s)을 같이 반환하는 기능
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

    /**
     * 모든 게시물을 조회할 때
     * QueryDsl의 페이징 문법 사용
     *
     * @return List Post
     * @throws NoSuchElementException
     */
    public List<PostResponse> findAll(SearchCondition condition, Pageable pageable) {
        List<Post> posts = queryFactory
                .select(post)
                .from(post)
                .leftJoin(post.account, account)
                .where(nameSearch(condition), tagSearch(condition))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return posts.stream()
                .map(p -> new PostResponse(
                        p.getAccount().getNickname(),
                        p.getCreatedDate(),
                        p.getContent(),
                        getLikeCount(p.getId()),
                        getCommentCount(p.getId())
                ))
                .collect(Collectors.toList());
    }

    private Long getLikeCount(Long postId) {
        return queryFactory
                .select(postLike.count())
                .from(postLike)
                .where(postLike.post.id.eq(postId))
                .fetchOne();
    }

    private Long getCommentCount(Long postId) {
        return queryFactory
            .select(postComment.count())
            .from(postComment)
            .where(postComment.post.id.eq(postId))
            .fetchOne();
    }
    private static BooleanExpression tagSearch(SearchCondition condition) {
        if (condition == null || condition.getName() == null || condition.getName().isEmpty()) {
            return Expressions.TRUE;
        }
        return postTag.tag.name.eq(condition.getTag());
    }

    private static BooleanExpression nameSearch(SearchCondition condition) {
        if (condition == null || condition.getName() == null || condition.getName().isEmpty()) {
            return Expressions.TRUE;
        }
        return post.account.nickname.eq(condition.getName());
    }
}
