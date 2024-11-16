package tmrv.dev.blogsystem.Services;


import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import tmrv.dev.blogsystem.entities.Role;
import tmrv.dev.blogsystem.entities.User;
import tmrv.dev.blogsystem.exception.UserNotFoundException;
import tmrv.dev.blogsystem.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
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
    public String deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
        return "Deleted user's id: " + id;
    }


}
