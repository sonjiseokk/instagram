package com.meta.instagram.domain.entity.repository.querydsl;

import com.meta.instagram.domain.dto.PostResponse;
import com.meta.instagram.domain.dto.SearchCondition;
import com.meta.instagram.domain.entity.Account;
import com.meta.instagram.domain.entity.Post;
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

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

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
        Post post = createTestData(1);

        //when
        PostResponse postResponse = postRepositoryQuery.findById(post.getId());

        //then
        assertThat(postResponse.getContent()).isEqualTo(post.getContent());
//        assertThat(postResponse.getTagNames().size()).isEqualTo(post.getPostTags().size());
//        assertTrue(postResponse.getTagNames().stream()
//                .anyMatch(postTag1 -> postTag1.equals("tag1")));

    }
    @Test
    @DisplayName("모든 게시물이 조회되는지 테스트")
    void 모든_게시물이_조회되는지_테스트() throws Exception {
        //given
        Pageable pageable = PageRequest.of(0, 10);
        Post post = createTestData(1);

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

    @Test
    @DisplayName("특정 게시물이 조회되는지 테스트")
    void 특정_게시물이_조회되는지_테스트() throws Exception {
        //given
        Pageable pageable = PageRequest.of(0, 10);
        Post post = createTestData(1);
        System.out.println(post.getPostTags().size());

        //when
        SearchCondition condition = SearchCondition.builder()
                .writerName("nickname")
                .tag("tag1")
                .build();
        List<PostResponse> postResponses = postRepositoryQuery.findAll(condition, pageable);
        //then
        assertThat(postResponses.size()).isEqualTo(1);

        PostResponse savedResponse = postResponses.get(0);
        assertThat(post.getAccount().getNickname()).isEqualTo(savedResponse.getNickname());
        assertThat(post.getCreatedDate()).isEqualTo(savedResponse.getCreatedDate());
        assertThat(post.getContent()).isEqualTo(savedResponse.getContent());
    }
    @Test
    @DisplayName("페이징이 정상적으로 처리되는지 테스트")
    void 페이징이_정상적으로_처리되는지_테스트() throws Exception {
        //given
        Pageable pageable = PageRequest.of(0, 10);
        List<Post> posts = new ArrayList<>();
        for (int i = 0; i <11; i++) {
            posts.add(createTestData(i));
        }

        //when
        List<PostResponse> responses = postRepositoryQuery.findAll(SearchCondition.builder().build(), pageable);
        PostResponse postResponse = responses.get(0);

        //then
        assertThat(posts.size()).isEqualTo(responses.size() + 1); // 11개 있고 2페이지이기 때문에 10개만 조회되어야 맞음
        assertThat(postResponse.getContent()).isEqualTo("content0");
        assertThat(responses.get(responses.size() - 1).getContent()).isEqualTo("content9");
    }
    // TODO: 라이크랑 코멘트 테이블이 없어서 지금은 테스트 불가능
    // 그러나 이후에 분명 한방 쿼리로 변경해야할 문제 (성능 이슈)
    @Test
    @DisplayName("한방 쿼리 테스트")
    void 한방_쿼리_테스트() throws Exception {
        //given
        Post post = createTestData(1);

        //when
        PostResponse postResponse = postRepositoryQuery.findByIdQuery(post.getId());

        //then
        assertThat(postResponse.getContent()).isEqualTo(post.getContent());
//        assertThat(postResponse.getComments()).isEqualTo(0);
    }
    private Post createTestData(int index) {
        Account account = getAccount();
        Tag tag = getTag(index);
        Post post = getPostData(tag, account, index);

        accountRepository.save(account);
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

    private static Tag getTag(int index) {
        return Tag.builder()
                .name("tag" + index)
                .build();
    }

    private static Post getPostData(Tag tag,Account account,int index) {
        Post post = Post.builder()
                .account(account)
                .content("content" + index)
                .build();

        post.addTag(tag);
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