package com.meta.instagram.domain.entity.repository.querydsl;

import com.meta.instagram.domain.entity.Post;
import com.meta.instagram.domain.entity.PostTag;
import com.meta.instagram.domain.entity.Tag;
import com.meta.instagram.domain.entity.repository.PostRepository;
import com.meta.instagram.domain.entity.repository.PostTagRepository;
import com.meta.instagram.domain.entity.repository.TagRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@Import({PostRepositoryQueryTest.TestConfig.class, PostRepositoryQuery.class})
class PostRepositoryQueryTest {
    @Autowired
    private PostRepositoryQuery postRepositoryQuery;
    @Autowired
    private PostTagRepository postTagRepository;
    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private EntityManager em;
    @Test
    @DisplayName("연관된 PostTag까지 같이 게시물 조회하는 기능 테스트")
    @Transactional
    void 연관된_PostTag까지_같이_게시물_조회하는_기능_테스트() throws Exception {
        //given
        Tag tag = getTag();
        Post post = getPostData(tag);
        PostTag postTag = getPostTag(post,tag);
        post.allocatePostTags(Set.of(postTag));

        postTagRepository.save(postTag);
        tagRepository.save(tag);
        postRepository.save(post);

        //when
        Post findPost = postRepositoryQuery.findById(post.getId());

        //then
        assertThat(findPost.getContent()).isEqualTo(post.getContent());
        assertThat(findPost.getPostTags().size()).isEqualTo(post.getPostTags().size());
        assertTrue(findPost.getPostTags().stream()
                .anyMatch(postTag1 -> postTag1.getTag().getName().equals("tag1")));

    }
    private static Tag getTag() {
        return Tag.builder()
                .name("tag1")
                .build();
    }

    private static PostTag getPostTag(Post post, Tag tag) {
        return PostTag.builder()
                .post(post)
                .tag(tag)
                .build();
    }

    private static Post getPostData(Tag tag) {
        Post post = Post.builder()
                .content("content")
                .postTags(null)
                .build();
        return post;
    }

    @TestConfiguration
    static class TestConfig{
        @PersistenceContext
        private EntityManager entityManager;

        @Bean
        @Primary
        public JPAQueryFactory jpaQueryFactory() {
            return new JPAQueryFactory(entityManager);
        }
    }
}