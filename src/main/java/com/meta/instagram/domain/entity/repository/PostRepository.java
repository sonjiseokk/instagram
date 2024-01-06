package com.meta.instagram.domain.entity.repository;

import com.meta.instagram.domain.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
//    @Query("select p.content, t.name from Post p join p.postTags pt join pt.tag t")
//    Optional<PostResponse> findByIdWithTag();
}
