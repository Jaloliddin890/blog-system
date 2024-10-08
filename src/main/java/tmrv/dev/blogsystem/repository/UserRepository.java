package tmrv.dev.blogsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;
import tmrv.dev.blogsystem.entities.Role;
import tmrv.dev.blogsystem.entities.User;

import java.util.List;
import java.util.Optional;

@Repository

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    List<User> findByRole(Role role);
    Optional<User> findByEmail(String email);


}
