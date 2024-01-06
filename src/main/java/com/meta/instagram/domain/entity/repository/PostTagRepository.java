package com.meta.instagram.domain.entity.repository;

import com.meta.instagram.domain.entity.PostTag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostTagRepository extends JpaRepository<PostTag, Long> {
}
