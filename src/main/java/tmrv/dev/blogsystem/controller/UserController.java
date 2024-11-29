package tmrv.dev.blogsystem.controller;


import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tmrv.dev.blogsystem.Services.PostService;
import tmrv.dev.blogsystem.Services.UserService;
import tmrv.dev.blogsystem.dtos.UserDtos.UpdateUserInfoRequest;
import tmrv.dev.blogsystem.entities.Post;

import java.util.List;

@RestController
@Tag(name = "User Controller", description = "User's features")

@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final PostService postService;

    public UserController(UserService userService, PostService postService) {
        this.userService = userService;
        this.postService = postService;
    }

    @PutMapping(value = "/cabinet/update/user-info", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> updateUserInfo(
            @Parameter(description = "Request body containing updated user information")
            @Valid @RequestParam(value = "username") String username,
            @RequestParam(value = "file", required = false) MultipartFile file) {

        UpdateUserInfoRequest updateUserInfoRequest = new UpdateUserInfoRequest();
        updateUserInfoRequest.setUsername(username);
        userService.updateUserInfo(updateUserInfoRequest, file);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/cabinet/getPosts")
    public ResponseEntity<List<Post>> getMyPosts() {
        List<Post> posts = postService.getCurrentUserPosts();
        return ResponseEntity.ok(posts);
    }



}



