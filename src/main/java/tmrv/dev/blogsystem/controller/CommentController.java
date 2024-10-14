package tmrv.dev.blogsystem.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;
import tmrv.dev.blogsystem.Services.CommentService;
import tmrv.dev.blogsystem.dtos.CommentDto;
import tmrv.dev.blogsystem.entities.Comment;


import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/{postId}/comment")
    public ResponseEntity<CommentDto> addComment(@PathVariable Long postId,
                                                 @RequestBody @Valid CommentDto commentDto) throws Exception {
        CommentDto createdComment = commentService.addComment(postId, commentDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdComment);
    }

    @GetMapping("/{id}/comments")
    public ResponseEntity<List<Comment>> getComments(@PathVariable Long id) throws Exception {
        List<Comment> comments = commentService.getCommentsByPostId(id);
        return ResponseEntity.ok(comments);
    }
}

