package com.meta.instagram.domain.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@Entity
public class Post {
    @Id
    @GeneratedValue
    @Column(name = "post_id")
    private Long id;
    private String content;
    @OneToMany
    private Set<PostTag> postTags = new HashSet<>();

    @Builder
    public Post(final String content, final Set<PostTag> postTags) {
        this.content = content;
        this.postTags = postTags;
    }
}
