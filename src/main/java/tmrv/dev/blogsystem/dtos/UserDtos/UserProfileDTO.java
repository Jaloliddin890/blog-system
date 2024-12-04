package tmrv.dev.blogsystem.dtos.UserDtos;

import jakarta.validation.constraints.Email;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

/**
 * DTO for {@link tmrv.dev.blogsystem.entities.User}
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserProfileDTO implements Serializable {

    private String username;

    @Email
    private String email;

    private MultipartFile profileImageUrl;
}