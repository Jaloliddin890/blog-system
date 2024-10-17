package tmrv.dev.blogsystem.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tmrv.dev.blogsystem.dtos.UserDto;
import tmrv.dev.blogsystem.dtos.UserDtoForLogin;
import tmrv.dev.blogsystem.Services.AuthService;

@RestController
@Tag(name = "Authentication Controller", description = "This Controller's apis provide more security")
public class AuthController {
    private final AuthService authService;


    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping(value = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> registerUser(@ModelAttribute UserDto userDto) {
        try {
            String token = authService.registerUser(userDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(token);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(
            @RequestBody UserDtoForLogin dto){
        return ResponseEntity.ok(authService.login(dto));
    }
}
