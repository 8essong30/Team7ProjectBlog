package com.sparta.blog.service;

import com.sparta.blog.category.Category;
import com.sparta.blog.category.CategoryRepository;
import com.sparta.blog.dto.request.PageDTO;
import com.sparta.blog.dto.request.PostRequestDto;
import com.sparta.blog.dto.response.PostResponseDto;
import com.sparta.blog.entity.Post;
import com.sparta.blog.entity.PostLike;
import com.sparta.blog.entity.User;
import com.sparta.blog.repository.PostLikeRepository;
import com.sparta.blog.repository.PostRepository;

import com.sparta.blog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    private final CategoryRepository categoryRepository;
    private final PostLikeRepository postLikeRepository;

    @Transactional
    public PostResponseDto createPost(PostRequestDto postRequestDto, User user) {

        System.out.println("PostService.createPost");
        System.out.println("user.getUsername() = " + user.getUsername());
        Category category = categoryRepository.findByName(postRequestDto.getCategory());

        // 요청받은 DTO 로 DB에 저장할 객체 만들기
        Post post = postRepository.saveAndFlush(new Post(postRequestDto, user,category));

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
    public List<PostResponseDto> getPagingPost(PageDTO dto) {
        int page = dto.getPage()-1;
        int size = dto.getSize();
        String sortBy = dto.getSortBy();
        boolean isAsc = dto.isAsc();
        // 페이징 처리
        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Post> posts = postRepository.findAll(pageable);
        List<PostResponseDto> postResponseDto = new ArrayList<>();
        for(Post post : posts){
            PostResponseDto postListDTO = new PostResponseDto(post);
            postResponseDto.add(postListDTO);
        }
        return postResponseDto;
        //요청할때 꼭 바디부분을 신경써주세요
        /*  양식
            {
            "page" : 2,
            "size" : 2,
            "sortBy" : "id",
            "isAsc" : true
            }
         */

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
    public ResponseEntity<String> likeOrDislikePost(Long id, String username) {
        Post post = postRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다.")
        );

        List<PostLike> postLikes = postLikeRepository.findByUsernameAndPostId(username, id);
        if (postLikes.isEmpty()) {
            PostLike postLike = postLikeRepository.save(new PostLike(username, post));
            return new ResponseEntity<>("해당 게시글에 좋아요를 했습니다.", HttpStatus.OK);
        } else {
            postLikeRepository.deleteByUsername(username);
            return new ResponseEntity<>("해당 게시글에 좋아요를 취소하였습니다.", HttpStatus.OK);
        }
    }

}