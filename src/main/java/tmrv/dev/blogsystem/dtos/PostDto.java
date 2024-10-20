package tmrv.dev.blogsystem.dtos;


import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

/**
 * DTO for {@link tmrv.dev.blogsystem.entities.Post}
 */
;

public record PostDto(
       @NotNull String title,
       String content,
       boolean blockComment) implements Serializable {
}
