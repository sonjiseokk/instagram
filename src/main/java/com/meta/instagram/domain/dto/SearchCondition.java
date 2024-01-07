package com.meta.instagram.domain.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class SearchCondition {
    private String tag;
    private String writerName;

    @Builder
    public SearchCondition(String tag, String writerName) {
        this.tag = tag;
        this.writerName = writerName;
    }
}
