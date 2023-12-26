package com.meta.instagram.service;

import com.meta.instagram.domain.dto.PostRequest;
import com.meta.instagram.domain.entity.Post;
import com.meta.instagram.domain.entity.PostTag;
import com.meta.instagram.domain.entity.Tag;
import com.meta.instagram.domain.entity.repository.PostRepository;
import com.meta.instagram.domain.entity.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class PostService {
    private final PostRepository postRepository;
    private final TagRepository tagRepository;

    @Transactional
    public Long createPost(PostRequest request) {
        Set<PostTag> postTags = new HashSet<>();

        Post post = Post.builder()
                .content(request.getContent())
                .postTags(postTags)
                .build();

        for (String tagName : request.getTags()) {
            Tag tag = tagRepository.findByName(tagName)
                    .orElseGet(() -> new Tag(tagName));

            tagRepository.save(tag);
            PostTag postTag = PostTag.builder()
                    .post(post)
                    .tag(tag)
                    .build();

            post.getPostTags().add(postTag);
        }
        postRepository.save(post);
        return post.getId();
    }
}
