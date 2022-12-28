package com.bowen.post;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class PostControllerTest {
    @MockBean
    private PostService service;
    @Autowired
    private MockMvc mockMvc;

    //Test Create post
    @Test
    void testGetPosts() throws Exception {
        // given
        Post post1 = new Post(1, 23, "This is my first post!", 3, LocalDateTime.now());
        Post post2 = new Post(2, 23, "Ooh, another cool post that I'm making", 1, LocalDateTime.now());
        doReturn(Lists.newArrayList(post1, post2)).when(service).getPosts(any());
        // when
        ResultActions result = mockMvc.perform(get("/api/v1/posts/23"));
        // then
        result.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
    //Test get posts
}
