package tmrv.dev.blogsystem.dtos;

import jakarta.validation.constraints.NotBlank;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link tmrv.dev.blogsystem.entities.Comment}
 */
public record CommentDto(
        @NotBlank String content
) implements Serializable {

}