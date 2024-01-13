package com.meta.instagram.domain.dto;

import com.meta.instagram.domain.entity.Image;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ImageDto {
    private String path;
    private String originalName;
    private String type;
    private Integer size;

    @Builder
    public ImageDto(Image image) {
        this.path = image.getPath();
        this.originalName = image.getOriginalName();
        this.type = image.getType();
        this.size = image.getSize();
    }
}
