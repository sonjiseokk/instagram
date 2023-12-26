package com.meta.instagram.domain.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.EnumType.STRING;
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
    private String nickname;
    private String password;
    @Enumerated(value = STRING)
    private Role role;
    @OneToMany(mappedBy = "account")
    private List<Post> posts = new ArrayList<>();

    @Builder
    public Account(final String email, final String nickname, final String password) {
        this.email = email;
        this.nickname = nickname;
        this.password = password;
        this.role = Role.ROLE_USER;
        this.posts = new ArrayList<>();
    }
}
