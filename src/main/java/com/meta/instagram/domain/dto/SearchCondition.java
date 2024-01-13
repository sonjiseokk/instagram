package com.meta.instagram.domain.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class SearchCondition {
    private List<String> tags;
    private String writerName;

    @Builder
    public SearchCondition(List<String> tags, String writerName) {
        this.tags = tags;
        this.writerName = writerName;
    }
}
