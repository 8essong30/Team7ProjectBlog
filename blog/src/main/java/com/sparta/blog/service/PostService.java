package com.sparta.blog.service;

import com.sparta.blog.dto.request.PostRequestDto;
import com.sparta.blog.dto.response.PostResponseDto;
import com.sparta.blog.entity.Post;
import com.sparta.blog.entity.PostLike;
import com.sparta.blog.entity.User;
import com.sparta.blog.repository.PostLikeRepository;
import com.sparta.blog.repository.PostRepository;

import com.sparta.blog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final PostLikeRepository postLikeRepository;

    @Transactional
    public PostResponseDto createPost(PostRequestDto postRequestDto, User user) {

        System.out.println("PostService.createPost");
        System.out.println("user.getUsername() = " + user.getUsername());

        // 요청받은 DTO 로 DB에 저장할 객체 만들기
        Post post = postRepository.saveAndFlush(new Post(postRequestDto, user));

        return new PostResponseDto(post);
    }

    @Transactional(readOnly = true)
    public List<PostResponseDto> getAllPost() {
        List<Post> posts = postRepository.findAllByOrderByModifiedAtDesc();
        List<PostResponseDto> postResponseDto = new ArrayList<>();
        for (Post post : posts){
            postResponseDto.add(new PostResponseDto(post));
        }
        return postResponseDto;
    }

    @Transactional(readOnly = true)
    public PostResponseDto getPosts(Long id) {
        Post post = postRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다.")
        );
        return new PostResponseDto(post);
    }

    @Transactional
    public PostResponseDto updatePost(Long id, PostRequestDto requestDto, User user) {

        System.out.println("PostService.updatePost");
        System.out.println("user.getUsername() = " + user.getUsername());

        Post post = postRepository.findByIdAndUserId(id, user.getId()).orElseThrow(
                () -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다.")
        );

        post.update(requestDto);
        return new PostResponseDto(post);
    }

    @Transactional
    public ResponseEntity<String> deletePost(Long id, User user) {

        Post post = postRepository.findByIdAndUserId(id, user.getId()).orElseThrow(
                () -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다.")
        );

        postRepository.deleteById(id);
        return new ResponseEntity<>("해당 게시글이 삭제되었습니다.", HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<String> likeOrDislikePost(Long id, User user) {
        Post post = postRepository.findByIdAndUserId(id, user.getId()).orElseThrow(
                () -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다.")
        );

        List<PostLike> postLikes = postLikeRepository.findByUsernameAndPostId(user.getUsername(), id);
        if (postLikes.isEmpty()) {
            PostLike postLike = postLikeRepository.save(new PostLike(user.getUsername(), post));
            return new ResponseEntity<>("해당 게시글에 좋아요를 했습니다.", HttpStatus.OK);
        } else {
            postLikeRepository.deleteByUsername(user.getUsername());
            return new ResponseEntity<>("해당 게시글에 좋아요를 취소하였습니다.", HttpStatus.OK);
        }
    }

}