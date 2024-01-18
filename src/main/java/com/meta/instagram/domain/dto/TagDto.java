package com.meta.instagram.domain.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class TagDto {
    private String tagName;

    @Builder
    public TagDto(String tagName) {
        this.tagName = tagName;
    }
}
