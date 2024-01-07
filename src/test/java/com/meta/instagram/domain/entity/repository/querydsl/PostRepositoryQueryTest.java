package com.meta.instagram.domain.entity.repository.querydsl;

import com.meta.instagram.domain.dto.PostResponse;
import com.meta.instagram.domain.dto.SearchCondition;
import com.meta.instagram.domain.entity.Account;
import com.meta.instagram.domain.entity.Post;
import com.meta.instagram.domain.entity.PostTag;
import com.meta.instagram.domain.entity.Tag;
import com.meta.instagram.domain.entity.repository.AccountRepository;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
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
    private AccountRepository accountRepository;
    @Autowired
    private EntityManager em;
    @Test
    @DisplayName("연관된 PostTag까지 같이 게시물 조회하는 기능 테스트")
    @Transactional
    void 연관된_PostTag까지_같이_게시물_조회하는_기능_테스트() throws Exception {
        //given
        Post post = createTestData();

        //when
        Post findPost = postRepositoryQuery.findById(post.getId());

        //then
        assertThat(findPost.getContent()).isEqualTo(post.getContent());
        assertThat(findPost.getPostTags().size()).isEqualTo(post.getPostTags().size());
        assertTrue(findPost.getPostTags().stream()
                .anyMatch(postTag1 -> postTag1.getTag().getName().equals("tag1")));

    }
    @Test
    @DisplayName("모든 게시물이 조회되는지 테스트")
    void 모든_게시물이_조회되는지_테스트() throws Exception {
        //given
        Pageable pageable = PageRequest.of(0, 10);
        Post post = createTestData();

        //when
        List<PostResponse> postResponses = postRepositoryQuery.findAll(SearchCondition.builder().build(), pageable);

        //then
        PostResponse savedResponse = postResponses.get(0);
        assertThat(post.getAccount().getNickname()).isEqualTo(savedResponse.getNickname());
        assertThat(post.getCreatedDate()).isEqualTo(savedResponse.getCreatedDate());
        assertThat(post.getContent()).isEqualTo(savedResponse.getContent());
//        assertThat(savedResponse.getCommentCount()).isEqualTo(1);
//        assertThat(savedResponse.getLikeCount()).isEqualTo(1);

    }
    private Post createTestData() {
        Account account = getAccount();
        Tag tag = getTag();
        Post post = getPostData(tag,account);
        PostTag postTag = getPostTag(post,tag);
        post.allocatePostTags(Set.of(postTag));

        accountRepository.save(account);
        postTagRepository.save(postTag);
        tagRepository.save(tag);
        postRepository.save(post);
        return post;
    }

    private static Account getAccount() {
        return Account.builder()
                .email("email")
                .nickname("nickname")
                .password("password")
                .build();
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

    private static Post getPostData(Tag tag,Account account) {
        return Post.builder()
                .account(account)
                .content("content")
                .postTags(null)
                .build();
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