package tmrv.dev.blogsystem.controller;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tmrv.dev.blogsystem.Services.UserService;
import tmrv.dev.blogsystem.dtos.UserDtos.UserProfileDTO;
import tmrv.dev.blogsystem.exception.UserNotFoundException;

@RestController
@Tag(name = "Admin Controller", description = "Admin's features")
public class AdminController {
    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/admin/blockUser/{id}")
    public ResponseEntity<String> blockUser(@PathVariable Long id) {
        try {
            String message = userService.blockUser(id);
            return ResponseEntity.ok(message);
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found: " + id);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/admin/unBlockUser/{id}")
    public ResponseEntity<String> unBlockUser(@PathVariable Long id) {
        try {
            String message = userService.unBlockUser(id);
            return ResponseEntity.ok(message);
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found: " + id);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @DeleteMapping("/admin/deleteUser/{userID}")
    public ResponseEntity<String> deleteUser(@PathVariable Long userID) {
        try {
            userService.deleteUser(userID);
            return ResponseEntity.ok("Deleted User: " + userID);
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found with ID: " + userID);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PatchMapping("/admin/updateUser/{userID}")
    public ResponseEntity<String> updateUser(@PathVariable Long userID,
                                             @Parameter @RequestParam(required = false) String username,
                                             @Parameter @RequestParam(required = false)  String email,
                                             @RequestParam(value = "file", required = false) MultipartFile file) {

        UserProfileDTO dto = new UserProfileDTO(username, email, file);
        userService.UserProfileUpdate(dto, userID, file);
        return ResponseEntity.ok("Updated User: " + userID);
    }


}
