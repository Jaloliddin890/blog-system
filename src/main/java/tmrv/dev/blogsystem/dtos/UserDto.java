package tmrv.dev.blogsystem.dtos;

import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link tmrv.dev.blogsystem.entities.User}
 */
public record UserDto(String username, String email,
                      String password, String confirmPassword,
                      String role,
                      MultipartFile profileImageUrl
                      )
        implements Serializable {
  }