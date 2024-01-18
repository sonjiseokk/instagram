package com.meta.instagram.domain.dto;

import lombok.Builder;

public class CommentDto {
    private String content;
    private String nickname;
    private String replyContent;

    @Builder
    public CommentDto(String content, String nickname, String replyContent) {
        this.content = content;
        this.nickname = nickname;
        this.replyContent = replyContent;
    }
}
