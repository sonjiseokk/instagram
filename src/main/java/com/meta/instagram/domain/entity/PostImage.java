package com.meta.instagram.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class PostImage {
    @Id
    @GeneratedValue
    @Column(name = "post_image_id")
    private Long id;
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "post_id")
    private Post post;
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "image_id")
    private Image image;

    @Builder
    public PostImage(Post post, Image image) {
        this.post = post;
        this.image = image;
    }
}
