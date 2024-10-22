package tmrv.dev.blogsystem.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import tmrv.dev.blogsystem.Services.UserService;
import tmrv.dev.blogsystem.exception.UserNotFoundException;

@RestController
@Tag(name = "User Controller", description = "CRUD operations for Users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
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
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
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
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
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
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }


}
