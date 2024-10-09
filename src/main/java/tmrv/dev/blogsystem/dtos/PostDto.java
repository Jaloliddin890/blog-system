package tmrv.dev.blogsystem.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

/**
 * DTO for {@link tmrv.dev.blogsystem.entities.Post}
 */
public record PostDto(@NotNull @NotEmpty String title,
                      @NotNull @NotEmpty String content,
                      boolean published) implements Serializable {
}