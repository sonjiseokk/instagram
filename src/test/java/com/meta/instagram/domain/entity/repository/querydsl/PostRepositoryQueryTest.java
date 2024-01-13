package com.meta.instagram.domain.entity.repository.querydsl;

import com.meta.instagram.domain.dto.PostResponse;
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

@DataJpaTest
@Import({PostRepositoryQueryTest.TestConfig.class, PostRepositoryQuery.class})
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
    @Transactional
    void 연관된_엔티티까지_같이_게시물_조회하는_기능_테스트() throws Exception {
        //given
        Post post = createTestData(1);

        //when
        Post findPost = postRepositoryQuery.findById(post.getId());

        //then
        // 연관 관계들이 잘 주입되었는지 테스트
        assertThat(findPost.getContent()).isEqualTo(post.getContent());
        assertThat(findPost.getPostTags().size()).isEqualTo(1);
        assertThat(findPost.getPostComments().size()).isEqualTo(1);
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
//    @Test
//    @DisplayName("한방 쿼리 테스트")
//    void 한방_쿼리_테스트() throws Exception {
//        //given
//        Post post = createTestData(1);
//
//        //when
//        PostResponse postResponse = postRepositoryQuery.findByIdQuery(post.getId());
//
//        //then
//        assertThat(postResponse.getContent()).isEqualTo(post.getContent());
////        assertThat(postResponse.getComments()).isEqualTo(0);
//    }
    // 테스트 데이터 생성 메서드
    private Post createTestData(int index) {
        Account account = getAccount();
        Tag tag = getTag(index);
        Image image = getImage();

        Comment comment = getComment(account, index);
        Post post = getPostData(tag, account, comment, image, index);

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
    private Comment getComment(Account account, int index) {
        Comment comment = Comment.builder()
                .account(account)
                .content("댓글" + index)
                .build();

        return comment;
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
    private static Post getPostData(Tag tag, Account account,
                                    Comment comment, Image image, int index) {
        Post post = Post.builder()
                .account(account)
                .content("content" + index)
                .build();

        addPostTag(post, tag);
        addPostComment(post,comment);
        addPostImage(post, image);
        addPostLike(post,account);

        return post;
    }

    // 댓글에 게시물 연결 및 게시물에 댓글 연결
    private static void addPostComment(Post post, Comment comment) {
        PostComment postComment = PostComment.builder()
                .comment(comment)
                .post(post)
                .build();

        comment.getPostComments().add(postComment);
        post.getPostComments().add(postComment);
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