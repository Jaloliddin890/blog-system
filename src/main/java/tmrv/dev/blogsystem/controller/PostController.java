package tmrv.dev.blogsystem.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tmrv.dev.blogsystem.Services.PostService;
import tmrv.dev.blogsystem.dtos.PostDto;
import tmrv.dev.blogsystem.entities.Post;
import tmrv.dev.blogsystem.entities.User;

@RestController
@Tag(name = "Post Controller", description = "This Post's apis provide CRUD operations")
@RequestMapping("/post")
@PreAuthorize(value = "hasRole('USER')")
public class PostController {
    private final PostService postService;
    private static final Logger log = LoggerFactory.getLogger(PostController.class);
    public PostController(PostService postService) {
        this.postService = postService;
    }


    @PostMapping("/createPost")
    public ResponseEntity<Post> createPost(@RequestBody @Valid PostDto postDto,
                                           @AuthenticationPrincipal User user) {

        if (user == null) {
            log.error("Authenticated user is null");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        log.info("Authenticated user: {}", user.getUsername());
        Post createdPost = postService.createPost(postDto, user);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPost);
    }

}
