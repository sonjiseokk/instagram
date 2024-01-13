package com.meta.instagram.domain.entity.repository.querydsl;

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

import static com.meta.instagram.domain.entity.QAccount.account;
import static com.meta.instagram.domain.entity.QPost.post;
import static com.meta.instagram.domain.entity.QPostComment.postComment;
import static com.meta.instagram.domain.entity.QPostImage.postImage;
import static com.meta.instagram.domain.entity.QPostLike.postLike;
import static com.meta.instagram.domain.entity.QPostTag.postTag;


@Repository
@RequiredArgsConstructor
public class PostRepositoryQuery{
    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    /**
     * 단건 게시물을 조회할때 연관된 태그들(s)을 같이 반환하는 기능
     *
     * @return post
     * @throws IllegalArgumentException
     */
    public Post findById(Long id) {
        Post findPost = queryFactory.query()
                .select(post)
                .from(post)
                .leftJoin(post.postTags, postTag)
                .leftJoin(post.postLikes, postLike)
                .leftJoin(post.postImages, postImage)
                .leftJoin(post.postComments, postComment)
                .where(post.id.eq(id))
                .fetchOne();

        if (findPost == null) {
            throw new IllegalArgumentException("게시물을 찾을 수 없습니다.");
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
    public List<Post> findAll(SearchCondition condition, Pageable pageable) {
        return queryFactory
                .select(post)
                .from(post)
                .leftJoin(post.account, account)
                .leftJoin(post.postComments, postComment)
                .leftJoin(post.postLikes, postLike)
                .leftJoin(post.postTags, postTag)
                .leftJoin(post.postImages, postImage)
                .where(nameSearch(condition), tagSearch(condition))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }


    private static BooleanExpression tagSearch(SearchCondition condition) {
        if (condition == null || condition.getTags() == null || condition.getTags().isEmpty()) {
            return null;
        }
        // TODO: 이부분이 GPT로 해결한 부분이라 어떻게 해결한 건지 찾아봐야할듯..
        List<String> tags = condition.getTags();
        BooleanExpression expression = Expressions.asBoolean(false).isFalse();

        for (String tag1 : tags) {
            expression = expression.or(post.postTags.any().tag.name.eq(tag1));
        }
        return expression;
    }

    private static BooleanExpression nameSearch(SearchCondition condition) {
        if (condition == null || condition.getWriterName() == null || condition.getWriterName().isEmpty()) {
            return null;
        }
        return account.nickname.eq(condition.getWriterName());
    }
}
