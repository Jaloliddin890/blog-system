package tmrv.dev.blogsystem.Services;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import tmrv.dev.blogsystem.entities.User;
import tmrv.dev.blogsystem.repository.UserRepository;

import java.util.Optional;

@Service
public class SessionService {

    private final UserRepository userRepository;

    public SessionService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getSession() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof UserDetails) {
                String username = ((UserDetails) principal).getUsername();
                Optional<User> userOptional = userRepository.findByUsername(username);
                return userOptional.orElseThrow(() -> new RuntimeException("User not found"));
            }
        }
        return null;
    }

}
