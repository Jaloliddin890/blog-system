package tmrv.dev.blogsystem.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Comment Controller", description = "Endpoints for managing comments on posts")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @Operation(summary = "Add a new comment", description = "Adds a new comment to a specific post by providing the post ID and comment data.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Comment created successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommentDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json"))
    })
    @PostMapping("/{postId}/addComment")
    public ResponseEntity<CommentDto> addComment(@PathVariable Long postId,
                                                 @RequestBody @Valid CommentDto commentDto) throws Exception {
        CommentDto createdComment = commentService.addComment(postId, commentDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdComment);
    }


    @Operation(summary = "Get comments for a post", description = "Retrieves all comments associated with a specific post ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comments retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Comment.class))),
            @ApiResponse(responseCode = "404", description = "Post not found",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/{postId}/getComments")
    public ResponseEntity<List<Comment>> getComments(@PathVariable Long postId) throws Exception {
        List<Comment> comments = commentService.getCommentsByPostId(postId);
        return ResponseEntity.ok(comments);
    }

    @Operation(summary = "Delete a comment", description = "Deletes a comment by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comment deleted successfully, returning the deleted comment ID",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Comment not found",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json"))
    })
    @PostMapping("{commentId}/deleteComment")
    public ResponseEntity<Long> deleteComment(@PathVariable Long commentId){
        Long deletedId = commentService.deleteComment(commentId);
        return ResponseEntity.ok(deletedId);
    }
}

