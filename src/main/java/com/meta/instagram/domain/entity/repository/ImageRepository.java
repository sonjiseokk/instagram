package com.meta.instagram.domain.entity.repository;

import com.meta.instagram.domain.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {

}
