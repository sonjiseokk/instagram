package com.meta.instagram.domain.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class PostResponse {
    private String content;
    private String tagName;
    private String username;
    private Long likeCount;
    private LocalDate createdDate;
    // TODO: 프로필 사진도 넣어야함
    // TODO: 게시물 사진도 넣어야함
    // TODO: 댓글 간략하게 맨 위 3개도 넣어야함

    @Builder
    public PostResponse(final String content, final String tagName, final String username, final Long likeCount, final LocalDate createdDate) {
        this.content = content;
        this.tagName = tagName;
        this.username = username;
        this.likeCount = likeCount;
        this.createdDate = createdDate;
    }
}
