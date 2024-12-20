package tmrv.dev.blogsystem.Services;

import org.springframework.stereotype.Service;
import tmrv.dev.blogsystem.dtos.CommentDto;
import tmrv.dev.blogsystem.entities.Comment;
import tmrv.dev.blogsystem.entities.Post;
import tmrv.dev.blogsystem.entities.User;
import tmrv.dev.blogsystem.exception.UserBlockedException;
import tmrv.dev.blogsystem.repository.CommentRepository;
import tmrv.dev.blogsystem.repository.PostRepository;

import java.util.List;


@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final SessionService sessionService;
    private final PostRepository postRepository;

    public CommentService(CommentRepository commentRepository, SessionService sessionService, PostRepository postRepository) {
        this.commentRepository = commentRepository;
        this.sessionService = sessionService;
        this.postRepository = postRepository;
    }

    public void addComment(Long postId, CommentDto commentDto) throws Exception {
        User user = sessionService.getSession();
        if (!user.isEnabled()) {
            throw new UserBlockedException();
        }
        Post post = postRepository.findById(postId).orElseThrow(() -> new Exception("Post not found"));
        if (post.isBlockComment()) {
            throw new Exception("Post is blocked");
        } else {
            Comment comment = new Comment();
            comment.setContent(commentDto.content());
            comment.setPost(post);
            comment.setUser(user);
            Comment savedComment = commentRepository.save(comment);

            new CommentDto(
                    savedComment.getContent()
            );
        }
    }

    public List<Comment> getCommentsByPostId(Long postId) throws Exception {
        Post post = postRepository.findById(postId).orElseThrow(() -> new Exception("Post not found"));
        return commentRepository.findByPost(post);
    }

    public Long deleteComment(Long id) {
        User user = sessionService.getSession();
        if (!user.isEnabled()) throw new UserBlockedException();
        commentRepository.findById(id).ifPresent(commentRepository::delete);
        return id;
    }
}
