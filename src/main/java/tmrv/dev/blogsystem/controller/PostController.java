package tmrv.dev.blogsystem.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tmrv.dev.blogsystem.Services.PostService;
import tmrv.dev.blogsystem.dtos.PostDto;
import tmrv.dev.blogsystem.entities.Post;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@Tag(name = "Post Controller", description = "CRUD operations for Posts")
public class PostController {
    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @Operation(summary = "Create a new post",
            description = "Create a new post with title, content, publication status, and an optional image file.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Post created successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request (validation error)"),
            @ApiResponse(responseCode = "500", description = "Internal server error")})
    @PreAuthorize("hasRole('USER')")
    @PostMapping(value = "/user/createPost", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Post> createPost(@Parameter @RequestParam String title,
                                           @Parameter @RequestParam String content,
                                           @Parameter @RequestParam boolean blockComment,
                                           @RequestParam(value = "file", required = false) MultipartFile file) {
        PostDto postDto = new PostDto(title, content, blockComment);
        Post post = postService.createPost(postDto, file);
        Map<String, Object> response = new HashMap<>();
        response.put("post", post);
        if (post.getImagePath() != null) {
            response.put("imagePath", post.getImagePath());
        }
        return ResponseEntity.ok(post);
    }

    @Operation(summary = "Update an existing post", description = "Updates a post's title, content, publication status, and optionally its image.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Post updated successfully"),
            @ApiResponse(responseCode = "404", description = "Post not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")})
    @PreAuthorize("hasRole('USER')")
    @PutMapping(value = "/user/updatePost/{postId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Post> updatePost(@PathVariable Long postId,
                                           @Parameter @RequestParam String title,
                                           @Parameter @RequestParam String content,
                                           @Parameter @RequestParam boolean blockComment,
                                           @RequestParam(value = "file", required = false) MultipartFile file) {
        PostDto postDto = new PostDto(title, content, blockComment);
        Post updatedPost = postService.updatePost(postId, postDto, file);
        return ResponseEntity.ok(updatedPost);
    }

    @Operation(summary = "Get a post by ID", description = "Retrieves a post's details using its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Post retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Post not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")})
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("/both/getPost/{id}")
    public ResponseEntity<Map<String, Object>> getPost(@PathVariable Long id) {
        Map<String, Object> response = postService.getPost(id);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Delete a post by ID", description = "Deletes a post using its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Post deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Post not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")})
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @DeleteMapping("/both/delete/{id}")
    public ResponseEntity<String> deletePost(@PathVariable Long id) {
        String deletePost = postService.deletePost(id);
        return ResponseEntity.ok(deletePost);
    }

    @Operation(summary = "Get all posts", description = "Retrieves a list of all posts.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Posts retrieved successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")})
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("/both/getAll")
    public ResponseEntity<List<Post>> getAll() {
        return ResponseEntity.ok(postService.getAllPosts());
    }

    @Operation(summary = "Download Image", description = "Downloads the image file. Accessible by Admin and User roles.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Image file downloaded successfully"),
            @ApiResponse(responseCode = "404", description = "Image not found", content = @Content)
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/both/download-image/{postID}")
    public ResponseEntity<byte[]> downloadImage(
            @Parameter(description = "ID of the image to download", required = true) @PathVariable Long postID) throws IOException {
        InputStream imageInputStream = postService.downloadImage(postID);
        byte[] imageBytes = imageInputStream.readAllBytes();
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=image.jpg");
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.IMAGE_JPEG_VALUE);
        headers.add(HttpHeaders.CONTENT_LENGTH, String.valueOf(imageBytes.length));
        return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
    }


}
