package com.meta.instagram.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@Entity
@NoArgsConstructor(access = PROTECTED)
@Getter
public class Image {
    @Id
    @GeneratedValue
    @Column(name = "image_id")
    private Long id;
    private String path;
    private String type;
    private long size;

    @Builder
    public Image(String path, String type, long size) {
        this.path = path;
        this.type = type;
        this.size = size;
    }

    public static Image getDefaultImage(){
        return Image.builder()
                .path("/")
                .size(0)
                .type("jpg")
                .build();
    }

}
