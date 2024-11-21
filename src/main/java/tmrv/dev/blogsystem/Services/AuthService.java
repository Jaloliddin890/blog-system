package tmrv.dev.blogsystem.Services;


import jakarta.transaction.Transactional;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tmrv.dev.blogsystem.dtos.UserDtos.UserDto;
import tmrv.dev.blogsystem.dtos.UserDtos.UserDtoForLogin;
import tmrv.dev.blogsystem.entities.Role;
import tmrv.dev.blogsystem.entities.User;
import tmrv.dev.blogsystem.repository.UserRepository;

import java.io.IOException;

@Service
public class AuthService {


    private final S3Service s3Service;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthService(S3Service s3Service, UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.s3Service = s3Service;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    @Transactional
    public String registerUser(UserDto userDto, MultipartFile file) throws Exception {
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
        if (userDto.role() == null) {
            user.setRole(Role.USER);
        } else {
            user.setRole(userDto.role());
        }

        user.setEnabled(true);
        String path = uploadFileToS3(file);
        user.setProfileImageUrl(path);
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

    private String uploadFileToS3(MultipartFile file) {
        try {
            String key = "filesForUser/" + System.currentTimeMillis() + "-" + file.getOriginalFilename();
            return s3Service.uploadFile(key, file.getInputStream(), file.getSize(), file.getContentType());
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload file", e);
        }
    }

}
