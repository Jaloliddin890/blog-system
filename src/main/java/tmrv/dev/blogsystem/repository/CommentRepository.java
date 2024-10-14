package tmrv.dev.blogsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tmrv.dev.blogsystem.entities.Comment;
import tmrv.dev.blogsystem.entities.Post;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByPost(Post post);
}