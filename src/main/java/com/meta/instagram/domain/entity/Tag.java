package com.meta.instagram.domain.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

import static lombok.AccessLevel.PROTECTED;

@NoArgsConstructor(access = PROTECTED)
@Getter
@Entity
public class Tag {
    @Id
    @GeneratedValue
    @Column(name = "tag_id")
    private Long id;
    @Column(name = "tag_name")
    private String name;
    @OneToMany(mappedBy = "tag")
    private Set<PostTag> postTags = new HashSet<>();

    @Builder
    public Tag(final String name) {
        this.name = name;
    }


}
