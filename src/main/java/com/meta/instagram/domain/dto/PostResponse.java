package com.meta.instagram.domain.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
public class PostResponse {
    private String content;
    private List<String> tagNames;
    private String username;
    private Long likeCount;
    private LocalDate createdDate;
    // TODO: 프로필 사진도 넣어야함
    // TODO: 게시물 사진도 넣어야함
    // TODO: 댓글 간략하게 맨 위 3개도 넣어야함

    @Builder
    public PostResponse(final String content, final List<String> tagNames, final String username, final Long likeCount, final LocalDate createdDate) {
        this.content = content;
        this.tagNames = tagNames;
        this.username = username;
        this.likeCount = likeCount;
        this.createdDate = createdDate;
    }
}
