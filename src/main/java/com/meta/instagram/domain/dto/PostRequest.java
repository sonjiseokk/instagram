package com.meta.instagram.domain.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class PostRequest {
    private String content;
    private List<String> tags;

    @Builder
    public PostRequest(final String content, final List<String> tags) {
        this.content = content;
        this.tags = tags;
    }
}
