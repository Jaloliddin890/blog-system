package tmrv.dev.blogsystem.controller;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tmrv.dev.blogsystem.Services.PostService;
import tmrv.dev.blogsystem.dtos.PostDto;
import tmrv.dev.blogsystem.entities.Post;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@Tag(name = "Post Controller", description = "This Post's apis provide CRUD operations")
@RequestMapping("/post")
@PreAuthorize(value = "hasRole('USER')")
public class PostController {
    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping(value = "/createPost", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, Object>> createPost(
            @Parameter @RequestParam String title,
            @Parameter @RequestParam String content,
            @Parameter @RequestParam boolean published,
            @RequestParam(value = "file", required = false) MultipartFile file) throws IOException {
        PostDto postDto = new PostDto(title, content, published);
        Post post = postService.createPost(postDto, file);

        Map<String, Object> response = new HashMap<>();
        response.put("post", post);

        if (post.getImagePath() != null) {
            response.put("imagePath", post.getImagePath());
        }

        return ResponseEntity.ok(response);
    }

    @PutMapping(value = "/updatePost/{postId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Post> updatePost(
            @PathVariable Long postId,
            @Parameter @RequestParam String title,
            @Parameter @RequestParam String content,
            @Parameter @RequestParam boolean published,
            @RequestParam(value = "file", required = false) MultipartFile file) throws IOException {
        PostDto postDto = new PostDto(title, content, published);
        Post updatedPost = postService.updatePost(postId, postDto, file);
        return ResponseEntity.ok(updatedPost);
    }

    @GetMapping("/getPost/{id}")
    public ResponseEntity<Map<String, Object>> getPost(@PathVariable Long id) {
        Map<String, Object> response = postService.getPost(id);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deletePost(@PathVariable Long id) {
        String deletePost = postService.deletePost(id);
        return ResponseEntity.ok(deletePost);
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<Post>> getAll() {
        return ResponseEntity.ok(postService.getAllPosts());
    }


}
