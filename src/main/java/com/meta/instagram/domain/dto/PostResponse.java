package com.meta.instagram.domain.dto;

import com.meta.instagram.domain.entity.Account;
import com.meta.instagram.domain.entity.Post;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

/**
 * 보여줘야 할 목록
 * 1. 프로필 이미지, 닉네임, 작성일
 * 2. 본문 사진(들), 글 내용 (콘텐츠)
 * 3. 좋아요 개수
 * 4. 댓글 개수, 댓글 상단에 3개
 * 5. 태그 이름(들)
 */
@Getter
public class PostResponse {
    // Image
    private List<ImageDto> images;

    // Account
    private ImageDto profileImage;
    private String nickname;

    // Post 기본 요소들
    private LocalDate createdDate;
    private String content;

    // 양방향 매핑 사용
    private int likeCount;
    private int commentCount;


    private List<CommentDto> comments;
    private List<String> tagNames;

    @Builder
    public PostResponse(List<ImageDto> images, ImageDto profileImage, Account account, Post post, List<CommentDto> comments, List<String> tagNames) {
        this.images = images;
        this.profileImage = profileImage;
        this.nickname = account.getNickname();
        this.createdDate = post.getCreatedDate();
        this.content = post.getContent();
        this.likeCount = post.getPostLikes().size();
        this.commentCount = post.getComments().size();
        this.comments = comments;
        this.tagNames = tagNames;
    }


}
