package com.meta.instagram.domain.entity.repository;

import com.meta.instagram.domain.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
