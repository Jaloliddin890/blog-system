package tmrv.dev.blogsystem.controller;


import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import tmrv.dev.blogsystem.Services.UserService;
import tmrv.dev.blogsystem.dtos.UserDtos.UpdateUserInfoRequest;

@RestController
@Tag(name = "User Controller", description = "User's features")

@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
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

}



