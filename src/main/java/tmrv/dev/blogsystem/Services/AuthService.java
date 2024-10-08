package tmrv.dev.blogsystem.Services;


import jakarta.transaction.Transactional;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tmrv.dev.blogsystem.dtos.UserDto;
import tmrv.dev.blogsystem.dtos.UserDtoForLogin;
import tmrv.dev.blogsystem.entities.User;
import tmrv.dev.blogsystem.repository.UserRepository;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    @Transactional
    public String registerUser(UserDto userDto) throws Exception {
        if (!userDto.password().equals(userDto.confirmPassword())) {
            throw new Exception("Passwords do not match");
        }
        if (userRepository.findByUsername(userDto.username()).isPresent()) {
            throw new Exception("Username is already taken");
        }
        if (userRepository.findByEmail(userDto.email()).isPresent()) {
            throw new Exception("Email is already registered");
        }
        User user = new User();
        user.setUsername(userDto.username());
        user.setEmail(userDto.email());
        user.setPassword(passwordEncoder.encode(userDto.password()));
        if (userDto.role() == null || userDto.role().isEmpty()) {
            user.setRole("ROLE_USER");
        } else {
            user.setRole(userDto.role());
        }
        user.setEnabled(true);
        userRepository.save(user);

        return jwtService.generateToken(user);
    }

    public String login(UserDtoForLogin dto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        dto.username(),
                        dto.password()
                )
        );
        User user = userRepository.findByUsername(dto.username()).orElseThrow();

        return jwtService.generateToken(user);
    }

}
