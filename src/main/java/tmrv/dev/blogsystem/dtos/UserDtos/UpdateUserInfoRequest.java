package tmrv.dev.blogsystem.dtos.UserDtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserInfoRequest {

    @NotEmpty(message = "Username is mandatory")
    @NotNull(message = "Username is mandatory")
    private String username;





}
