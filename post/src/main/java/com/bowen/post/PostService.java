package com.bowen.post;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final RestTemplate restTemplate;

    public Post createPost(PostCreationRequest request) {
        Post post = Post.builder()
                .userId(request.userId())
                .postBody(request.postBody())
                .likes(0)
                .createdAt(LocalDateTime.now())
                .build();
        // todo: verify user exists
        UserExistsResponse response = restTemplate.getForObject(
                "http://USER/api/v1/users/{userId}",
                UserExistsResponse.class,
                request.userId()
        );
        if(response.doesExist()) {
            return postRepository.saveAndFlush(post);
        }
        // todo: validate post body
        return null;
    }

    public List<Post> getPosts(Integer userId){
       return postRepository.findByUserId(userId);
    }
}
