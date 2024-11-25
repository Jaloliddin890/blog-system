package tmrv.dev.blogsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import tmrv.dev.blogsystem.entities.User;

import java.util.Optional;

@Repository

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);

    Optional<User> findByEmail(String email);


}
