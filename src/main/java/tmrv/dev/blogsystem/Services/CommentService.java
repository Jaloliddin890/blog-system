package tmrv.dev.blogsystem.Services;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import tmrv.dev.blogsystem.dtos.CommentDto;
import tmrv.dev.blogsystem.entities.Comment;
import tmrv.dev.blogsystem.entities.Post;
import tmrv.dev.blogsystem.entities.User;
import tmrv.dev.blogsystem.repository.CommentRepository;
import tmrv.dev.blogsystem.repository.PostRepository;
import tmrv.dev.blogsystem.repository.UserRepository;

import java.util.List;


@Service
public class CommentService {
    private final CommentRepository commentRepository;

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public CommentService(CommentRepository commentRepository, PostRepository postRepository, UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    public CommentDto addComment(Long postId,  CommentDto commentDto) throws Exception {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        Post post = postRepository.findById(postId).orElseThrow(() -> new Exception("Post not found"));
        Comment comment = new Comment();
        comment.setContent(commentDto.content());
        comment.setPost(post);
        comment.setUser(user);
        Comment savedComment = commentRepository.save(comment);

        return new CommentDto(
                savedComment.getContent()
        );
    }

    public List<Comment> getCommentsByPostId(Long postId) throws Exception {
        Post post = postRepository.findById(postId).orElseThrow(() -> new Exception("Post not found"));
        return commentRepository.findByPost(post);
    }
}
