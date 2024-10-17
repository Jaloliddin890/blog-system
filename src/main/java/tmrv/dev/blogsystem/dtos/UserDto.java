package tmrv.dev.blogsystem.dtos;

import org.springframework.web.multipart.MultipartFile;
import tmrv.dev.blogsystem.entities.Role;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link tmrv.dev.blogsystem.entities.User}
 */
public record UserDto(String username, String email,
                      String password, String confirmPassword,
                      Role role,
                      MultipartFile profileImageUrl)
        implements Serializable {
  }