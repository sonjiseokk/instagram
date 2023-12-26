package com.meta.instagram.domain.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@NoArgsConstructor(access = PROTECTED)
@Getter
@Entity
public class PostTag {
    @Id
    @GeneratedValue
    @Column(name = "post_tag_id")
    private Long id;
    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "tag_id")
    private Tag tag;

    @Builder
    public PostTag(final Post post, final Tag tag) {
        this.post = post;
        this.tag = tag;
    }
}
