package com.meta.instagram.domain.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

/**
 * 보여줘야 할 목록
 * 1. 프로필 이미지, 닉네임, 작성일
 * 2. 본문 사진(들), 글 내용 (콘텐츠)
 * 3. 좋아요 개수
 * 4. 댓글 개수, 댓글 상단에 3개
 * 5. 태그 이름(들)
 */
@Getter
public class PostResponse {
    private MultipartFile profileImage; // 서비스
    private String nickname;
    private LocalDate createdDate;
    private List<MultipartFile> images; // 서비스
    private String content;
    private Long likeCount;
    private Long commentCount;
    private List<String> comments; // 서비스, 다른 쿼리 (아마 commentDto 따로 만들어야할듯?)
    private List<String> tagNames;

    // TODO: 이미지들, 코멘트들, 태그이름들 서비스에서 처리해서 주입해주기


    @Builder
    @QueryProjection
    public PostResponse(String nickname, LocalDate createdDate, String content, Long likeCount, Long commentCount) {
        this.nickname = nickname;
        this.createdDate = createdDate;
        this.content = content;
        this.likeCount = likeCount;
        this.commentCount = commentCount;
    }
}
