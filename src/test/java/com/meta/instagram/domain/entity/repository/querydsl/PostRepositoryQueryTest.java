package com.meta.instagram.domain.entity.repository.querydsl;

import com.meta.instagram.domain.dto.SearchCondition;
import com.meta.instagram.domain.entity.*;
import com.meta.instagram.domain.entity.repository.*;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 쿼리 DSL을 사용하는 레포지토리를 테스트
 *
 * @author 손지석
 * @version jdk17
 */
@DataJpaTest
@Import({PostRepositoryQueryTest.TestConfig.class, PostRepositoryQuery.class})
@Transactional
class PostRepositoryQueryTest {
    @Autowired
    private PostRepositoryQuery postRepositoryQuery;
    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private ImageRepository imageRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private EntityManager em;
    @Test
    @DisplayName("연관된 엔티티까지 같이 게시물 조회하는 기능 테스트")
    void 연관된_엔티티까지_같이_게시물_조회하는_기능_테스트() throws Exception {
        //given
        Post post = createTestData(1);

        //when
        Post findPost = postRepositoryQuery.findById(post.getId());

        //then
        // 연관 관계들이 잘 주입되었는지 테스트
        assertThat(findPost.getContent()).isEqualTo(post.getContent());
        assertThat(findPost.getComments().size()).isEqualTo(post.getComments().size());
        assertThat(findPost.getPostTags().size()).isEqualTo(1);
        assertThat(findPost.getPostImages().size()).isEqualTo(1);
        assertThat(findPost.getPostLikes().size()).isEqualTo(1);

        // 실제로 그 값이 내가 생성한 데이터인지 테스트
        assertTrue(findPost.getPostTags().stream()
                .anyMatch(postTag1 -> postTag1.getTag().getName().equals("tag1"))
        );
    }
    @Test
    @DisplayName("모든 게시물이 조회되는지 테스트")
    void 모든_게시물이_조회되는지_테스트() throws Exception {
        //given
        Pageable pageable = PageRequest.of(0, 10);
        Post post = createTestData(1);

        //when
        List<Post> posts = postRepositoryQuery.findAll(SearchCondition.builder().build(), pageable);

        //then
        Post post1 = posts.get(0);
        assertThat(posts.size()).isEqualTo(1);
        assertThat(post1.getContent()).isEqualTo("content1");
    }

    @Test
    @DisplayName("특정 게시물이 조회되는지 테스트")
    void 특정_게시물이_조회되는지_테스트() throws Exception {
        //given
        Pageable pageable = PageRequest.of(0, 10);
        Post post = createTestData(1);

        //when
        SearchCondition condition = SearchCondition.builder()
                .writerName("nickname")
                .tags(List.of("tag1"))
                .build();
        List<Post> posts = postRepositoryQuery.findAll(condition, pageable);

        //then
        assertThat(posts.size()).isEqualTo(1);

        Post findPost = posts.get(0);
        assertThat(findPost.getAccount().getNickname()).isEqualTo("nickname");
        findPost.getPostTags().stream()
                .map(postTag -> assertThat(postTag.getTag().getName()).isEqualTo("tag1"));
    }
    @Test
    @DisplayName("페이징이 정상적으로 처리되는지 테스트")
    void 페이징이_정상적으로_처리되는지_테스트() throws Exception {
        //given
        Pageable pageable = PageRequest.of(0, 10);
        List<Post> initPosts = new ArrayList<>();
        for (int i = 0; i <11; i++) {
            initPosts.add(createTestData(i));
        }

        //when
        List<Post> posts = postRepositoryQuery.findAll(SearchCondition.builder().build(), pageable);
        Post firstPost = posts.get(0);
        //then
        assertThat(posts.size()).isEqualTo(initPosts.size() - 1); // 11개 있고 2페이지이기 때문에 10개만 조회되어야 맞음
        assertThat(posts.get(posts.size() - 1).getContent()).isEqualTo("content9");
    }

    /**
     * 테스트 생성 메서드
     * 1. 어카운트 생성
     * 2. 태그 생성
     * 3. 디폴트 이미지 생성
     * 4. 어카운트, 태그, 이미지 객체를 기반으로 Post 객체 생성
     * 5. Post 객체를 생성 후 Comment 객체 생성 (원래 시스템 적으로 Post -> Comment 순이기 때문)
     * 6. Post 객체의 comments에 comment를 추가
     * 7. 레포지토리를 통해 모두 저장
     * @param index
     * @return 모든 연관관계를 가진 Post 객체 반환
     */
    private Post createTestData(int index) {
        Account account = getAccount();
        Tag tag = getTag(index);
        Image image = getImage();

        Post post = getPostData(tag, account, image, index);
        Comment comment = getComment(post, account,index);
        post.addComment(comment);

        saveEntities(image, account, tag, post, comment);

        return post;
    }

    // 모든 엔티티 저장
    private void saveEntities(Image image, Account account, Tag tag, Post post, Comment comment) {
        imageRepository.save(image);
        accountRepository.save(account);
        tagRepository.save(tag);
        postRepository.save(post);
        commentRepository.save(comment);
    }

    // 댓글 데이터 생성
    private Comment getComment(Post post, Account account, int index) {
        return Comment.builder()
                .post(post)
                .account(account)
                .content("댓글" + index)
                .build();
    }

    // 이미지 데이터 생성
    private Image getImage() {
        return Image.getDefaultImage();
    }

    // 계정 데이터 생성
    private static Account getAccount() {
        return Account.builder()
                .email("email")
                .nickname("nickname")
                .password("password")
                .build();
    }

    // 태그 데이터 생성
    private static Tag getTag(int index) {
        return Tag.builder()
                .name("tag" + index)
                .build();
    }

    // 게시물 데이터 생성
    private static Post getPostData(Tag tag, Account account, Image image, int index) {
        Post post = Post.builder()
                .account(account)
                .content("content" + index)
                .build();

        addPostTag(post, tag);
        addPostImage(post,image);
        addPostLike(post, account);

        return post;
    }


    private static void addPostImage(Post post, Image image) {
        PostImage postImage = PostImage.builder()
                .image(image)
                .post(post)
                .build();

        post.getPostImages().add(postImage);
    }

    private static void addPostLike(Post post,Account account) {
        PostLike postLike = PostLike.builder()
                .account(account)
                .post(post)
                .build();
        post.getPostLikes().add(postLike);
    }

    // 게시물에 태그 연결
    private static void addPostTag(Post post, Tag tag) {
        PostTag postTag = PostTag.builder()
                .tag(tag)
                .post(post)
                .build();
        post.getPostTags().add(postTag);
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