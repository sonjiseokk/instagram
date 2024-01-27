package com.meta.instagram.domain.dto;

import com.meta.instagram.domain.entity.Image;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ImageDto {
    private String path;
    private String type;
    private long size;

    @Builder
    public ImageDto(Image image) {
        this.path = image.getPath();
        this.type = image.getType();
        this.size = image.getSize();
    }
}
