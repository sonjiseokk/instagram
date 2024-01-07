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
    private String originalName;
    private String type;
    private Integer size;

    @Builder
    public Image(String path, String originalName, String type, Integer size) {
        this.path = path;
        this.originalName = originalName;
        this.type = type;
        this.size = size;
    }
}
