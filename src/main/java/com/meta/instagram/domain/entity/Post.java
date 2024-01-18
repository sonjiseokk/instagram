package com.meta.instagram.domain.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@Entity
public class Post extends BaseEntity{
    @Id
    @GeneratedValue
    @Column(name = "post_id")
    private Long id;
    private String content;
    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;
    @OneToMany(mappedBy = "post")
    private Set<PostTag> postTags = new HashSet<>();
    @OneToMany(mappedBy = "post")
    private List<PostImage> postImages = new ArrayList<>();
    @OneToMany(mappedBy = "post")
    private List<PostLike> postLikes = new ArrayList<>();
    @OneToMany(mappedBy = "post")
    private List<Comment> comments = new ArrayList<>();

    @Builder
    public Post(String content, Account account) {
        this.content = content;
        this.account = account;
    }
    public void addComment(Comment comment){
        this.comments.add(comment);
    }
}
