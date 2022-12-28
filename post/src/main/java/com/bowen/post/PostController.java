package com.bowen.post;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("api/v1/posts")
public class PostController {

    private final PostService postService;

    @PostMapping
    public PostResponse createPost(@RequestBody PostCreationRequest request) {
        log.info("new post creation {}", request);
        Post post = postService.createPost(request);
        return new PostResponse(post);
    }

    @GetMapping(path="{userId}")
    public PostListResponse getPosts(@PathVariable("userId") Integer userId) {
        log.info("fetching posts for user {}", userId);
        List<Post> posts = postService.getPosts(userId);
        return new PostListResponse(posts);
    }
}
