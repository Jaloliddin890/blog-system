package tmrv.dev.blogsystem.Services;


import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tmrv.dev.blogsystem.dtos.UserDtos.UpdateUserInfoRequest;
import tmrv.dev.blogsystem.entities.Role;
import tmrv.dev.blogsystem.entities.User;
import tmrv.dev.blogsystem.exception.UserNotFoundException;
import tmrv.dev.blogsystem.repository.UserRepository;

import java.io.IOException;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final SessionService sessionService;
    private final S3Service s3Service;

    public UserService(UserRepository userRepository, SessionService sessionService, S3Service s3Service) {
        this.userRepository = userRepository;
        this.sessionService = sessionService;
        this.s3Service = s3Service;
    }


    @Transactional
    public String blockUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
        if (user.getRole().equals(Role.ADMIN)) {
            return "Cannot block admin role. Admin ID: " + user.getId();
        }
        user.setEnabled(false);
        userRepository.save(user);
        return "Blocked user's id: " + user.getId();
    }

    @Transactional
    public String unBlockUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
        if (user.getRole().equals(Role.ADMIN)) {
            return "Cannot block admin role. Admin ID: " + user.getId();
        }
        user.setEnabled(true);
        userRepository.save(user);
        return "Blocked user's id: " + user.getId();
    }

    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }


    public void updateUserInfo(UpdateUserInfoRequest updateUserInfoRequest, MultipartFile file) {
        User user = sessionService.getSession();

        if (user != null) {

            user.setUsername(updateUserInfoRequest.getUsername());


            if (file != null) {
                String path = uploadFileToS3(file);
                user.setProfileImageUrl(path);
            }

            // Save the updated user to the repository
            userRepository.save(user);
        } else {
            throw new RuntimeException("User not found in session");
        }
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
