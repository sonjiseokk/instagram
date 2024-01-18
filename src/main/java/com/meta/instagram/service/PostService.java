package com.meta.instagram.service;

import com.meta.instagram.domain.dto.CommentDto;
import com.meta.instagram.domain.dto.ImageDto;
import com.meta.instagram.domain.dto.PostResponse;
import com.meta.instagram.domain.dto.TagDto;
import com.meta.instagram.domain.entity.*;
import com.meta.instagram.domain.entity.repository.PostRepository;
import com.meta.instagram.domain.entity.repository.querydsl.PostRepositoryQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class PostService {
    private final PostRepository postRepository;
    private final PostRepositoryQuery postRepositoryQuery;

    /**
     * 특정 게시물의 모든 정보를 볼 수 있는 서비스
     * 1. 프로필 이미지, 닉네임, 작성일
     * 2. 본문 사진(들), 글 내용 (콘텐츠)
     * 3. 좋아요 개수
     * 4. 댓글 개수, 댓글 내용들
     * 5. 태그 이름(들)
     *
     * @param postId 찾는 게시물 아이디
     * @return DTO 형태로 감싼 PostResponse
     */
    public PostResponse viewPostDetail(Long postId) {
        Post post = postRepositoryQuery.findById(postId);

        return PostResponse.builder()
                .images(convertPostImages(post.getPostImages()))
                .profileImage(createImageDto(post.getAccount().getProfileImage()))
                .account(post.getAccount())
                .post(post)
                .comments(convertComments(post.getComments()))
                .tagNames(convertPostTags(post.getPostTags()))
                .build();
    }

    private static List<TagDto> convertPostTags(Set<PostTag> postTags) {
        return postTags.stream()
                .map(postTag -> postTag.getTag())
                .map(tag -> TagDto.builder().tagName(tag.getName()).build())
                .collect(Collectors.toList());
    }

    private static List<CommentDto> convertComments(List<Comment> comments) {
        return comments.stream()
                .map(comment -> createCommentDto(comment))
                .collect(Collectors.toList());
    }

    private static List<ImageDto> convertPostImages(List<PostImage> postImages) {
        return postImages.stream()
                .map(postImage -> postImage.getImage())
                .map(image -> createImageDto(image))
                .collect(Collectors.toList());
    }

    private static ImageDto createImageDto(Image image) {
        return ImageDto.builder()
                .image(image)
                .build();
    }

    private static CommentDto createCommentDto(Comment comment) {
        return CommentDto.builder()
                .content(comment.getContent())
                .nickname(comment.getAccount().getNickname())
                .replyContent(null)
                .build();
    }


}
