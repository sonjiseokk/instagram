package com.meta.instagram.service;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {
//    @Mock
//    private PostRepository postRepository;
//    @Mock
//    private TagRepository tagRepository;
//    @InjectMocks
//    private PostService postService;
//
//    @Test
//    @DisplayName("게시물 작성_태그존재")
//    void 게시물_작성_태그존재() throws Exception {
//        //given
//        PostRequest postRequest = PostRequest.builder()
//                .content("너무 좋아요~")
//                .tags(List.of("A", "B", "C"))
//                .build();
//        when(tagRepository.findByName("A")).thenReturn(Optional.of(new Tag("A")));
//        when(tagRepository.findByName("B")).thenReturn(Optional.of(new Tag("B")));
//        when(tagRepository.findByName("C")).thenReturn(Optional.of(new Tag("C")));
//
//        //when
//        Long createdPostId = postService.createPost(postRequest);
//        //then
//        verify(postRepository, times(1)).save(any(Post.class));
//        verify(tagRepository, times(3)).findByName(anyString());
//    }
//
//    @Test
//    @DisplayName("게시물 작성 태그 없음")
//    void 게시물_작성_태그_없음() throws Exception {
//        //given
//        PostRequest postRequest = PostRequest.builder()
//                .content("너무 좋아요~")
//                .tags(List.of("A", "B", "C"))
//                .build();
//        when(tagRepository.findByName("A")).thenReturn(Optional.empty());
//        when(tagRepository.findByName("B")).thenReturn(Optional.empty());
//        when(tagRepository.findByName("C")).thenReturn(Optional.empty());
//
//        //when
//        Long createdPostId = postService.createPost(postRequest);
//        //then
//        verify(postRepository, times(1)).save(any(Post.class));
//        verify(tagRepository, times(3)).findByName(anyString());
//        verify(tagRepository, times(3)).save(any(Tag.class));
//    }

}