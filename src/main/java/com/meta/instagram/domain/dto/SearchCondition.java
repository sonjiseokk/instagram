package com.meta.instagram.domain.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class SearchCondition {
    private String tag;
    private String name;

    @Builder
    public SearchCondition(String tag, String name) {
        this.tag = tag;
        this.name = name;
    }
}
