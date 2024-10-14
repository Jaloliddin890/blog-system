package tmrv.dev.blogsystem.controller;



import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
;
import org.springframework.web.bind.annotation.*;
import tmrv.dev.blogsystem.Services.PostService;
import tmrv.dev.blogsystem.dtos.PostDto;
import tmrv.dev.blogsystem.entities.Post;



@RestController
@Tag(name = "Post Controller", description = "This Post's apis provide CRUD operations")
@RequestMapping("/post")
@PreAuthorize(value = "hasRole('USER')")
public class PostController {
    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping("/create")
    public ResponseEntity<Post> createPost(@RequestBody PostDto postDto) {
        Post post = postService.createPost(postDto);
        return ResponseEntity.ok(post);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Post> updatePost(@RequestBody PostDto postDto, @PathVariable Long id) {
        Post updatedPost = postService.updatePost(postDto, id);
        return ResponseEntity.ok(updatedPost);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deletePost( @PathVariable Long id) {
        String deletePost = postService.deletePost( id);
        return ResponseEntity.ok(deletePost);
    }

    @GetMapping
    public ResponseEntity<Page<Post>> getAllPosts(Pageable pageable) {
        Page<Post> posts = postService.getPosts(pageable);
        return ResponseEntity.ok(posts);
    }


}
