package tmrv.dev.blogsystem.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import tmrv.dev.blogsystem.Services.AuthService;
import tmrv.dev.blogsystem.dtos.UserDto;
import tmrv.dev.blogsystem.dtos.UserDtoForLogin;
import tmrv.dev.blogsystem.entities.Role;

@RestController
@Tag(name = "Authentication Controller", description = "This Controller's apis provide more security")
public class AuthController {
    private final AuthService authService;


    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Operation(summary = "Register a new user", description = "Registers a new user with a username, email, password, and role. " +
            "Optionally, a profile picture can be uploaded.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User registered successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request (For example: validation errors)")
    })
    @PostMapping(value = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> registerUser(
            @Parameter @RequestParam String username,
            @Parameter @RequestParam String email,
            @Parameter @RequestParam String password,
            @Parameter @RequestParam String confirmPassword,
            @Parameter @RequestParam Role role,
            @RequestParam(value = "file", required = false) MultipartFile file) {
        UserDto userDto = new UserDto(username, email, password, confirmPassword, role, file);
        try {
            String token = authService.registerUser(userDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(token);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Login a user", description = "Logs in an existing user with username and password, returning a JWT token on success.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login successful"),
            @ApiResponse(responseCode = "401", description = "Unauthorized (invalid credentials)")
    })
    @PostMapping("/login")
    public ResponseEntity<String> login(
            @RequestBody UserDtoForLogin dto) {
        return ResponseEntity.ok(authService.login(dto));
    }
}
