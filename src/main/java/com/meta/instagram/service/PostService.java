package com.meta.instagram.service;

import com.meta.instagram.domain.dto.ImageDto;
import com.meta.instagram.domain.dto.PostResponse;
import com.meta.instagram.domain.entity.*;
import com.meta.instagram.domain.entity.repository.PostRepository;
import com.meta.instagram.domain.entity.repository.TagRepository;
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
    private final TagRepository tagRepository;

    public PostResponse findById(Long id) {
        Post post = postRepositoryQuery.findById(id);

        // 이미지 엔티티 추출
        List<PostImage> postImages = post.getPostImages();
        List<ImageDto> imageDtos = convertPostImageToImage(postImages);

        // account 엔티티 추출
        Account account = post.getAccount();
        Image profileImage = account.getProfileImage();

        ImageDto profileImageDto = ImageDto.builder()
                .image(profileImage)
                .build();

        List<PostComment> postComments = post.getPostComments();
        List<Comment> comments = convertPostCommentToComment(postComments);

        Set<PostTag> postTags = post.getPostTags();
        List<String> tagNames = convertPostTagsToTagNames(postTags);

        PostResponse.builder()
                .images(imageDtos)
                .profileImage(profileImageDto)
                .account(account)
                .post(post)
                .comments()

    }

    private static List<String> convertPostTagsToTagNames(Set<PostTag> postTags) {
        return postTags.stream()
                .map(postTag -> postTag.getTag().getName())
                .collect(Collectors.toList());
    }

    private static List<Comment> convertPostCommentToComment(List<PostComment> postComments) {
        return postComments.stream()
                .map(postComment -> postComment.getComment())
                .collect(Collectors.toList());
    }

    private static List<ImageDto> convertPostImageToImage(List<PostImage> postImages) {
        return postImages.stream()
                .map(postImage -> ImageDto.builder().image(postImage.getImage()).build())
                .collect(Collectors.toList());

    }
}
