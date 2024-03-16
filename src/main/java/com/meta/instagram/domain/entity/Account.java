package com.meta.instagram.domain.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;


@Getter
@NoArgsConstructor(access = PROTECTED)
@Entity
public class Account extends BaseEntity{
    @Id
    @GeneratedValue
    @Column(name = "account_id")
    private Long id;
    private String email;
    private String username;
    private String nickname;
    private String password;
    @Enumerated(value = STRING)
    private Role role;
    @OneToMany(mappedBy = "account")
    private List<Post> posts = new ArrayList<>();
    @ManyToOne(fetch = LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name = "image_id")
    private Image profileImage;
    @Builder

    public Account(String email, String username, String nickname, String password) {
        this.email = email;
        this.username = username;
        this.nickname = nickname;
        this.password = password;
        this.role = Role.ROLE_USER;
        this.profileImage = Image.getDefaultImage();
    }
    public void changeProfileImage(Image image){
        this.profileImage = image;
    }
}
