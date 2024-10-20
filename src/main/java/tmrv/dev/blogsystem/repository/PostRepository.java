package tmrv.dev.blogsystem.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import tmrv.dev.blogsystem.entities.Post;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {



    Optional<Post> findById(Long aLong);

    Page<Post> findAll(Pageable pageable);
}