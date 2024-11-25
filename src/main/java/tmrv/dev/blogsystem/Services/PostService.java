package tmrv.dev.blogsystem.Services;


import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tmrv.dev.blogsystem.dtos.PostDto;
import tmrv.dev.blogsystem.entities.Post;
import tmrv.dev.blogsystem.entities.User;
import tmrv.dev.blogsystem.exception.ResourceNotFoundException;
import tmrv.dev.blogsystem.exception.UserBlockedException;
import tmrv.dev.blogsystem.repository.PostRepository;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PostService {


    private final S3Service s3Service;
    private final PostRepository postRepository;
    private final SessionService sessionService;

    public PostService(S3Service s3Service, PostRepository postRepository, SessionService sessionService) {
        this.s3Service = s3Service;
        this.postRepository = postRepository;
        this.sessionService = sessionService;
    }

    public Post createPost(PostDto postDto, MultipartFile file) {
        User user = sessionService.getSession();
        if (!user.isEnabled()) throw new UserBlockedException();
        String path = uploadFileToS3(file);
        Post post = new Post();
        post.setTitle(postDto.title());
        post.setContent(postDto.content());
        post.setBlockComment(postDto.blockComment());
        post.setUser(user);
        post.setImagePath(path);
        return post;
    }


    public Post updatePost(Long postId, PostDto postDto, MultipartFile file) {
        Post existingPost = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        User user = sessionService.getSession();
        if (!user.isEnabled()) throw new UserBlockedException();
        if (!existingPost.getUser().getId().equals(user.getId()))
            throw new RuntimeException("You are not authorized to update this post");
        String path = uploadFileToS3(file);
        existingPost.setTitle(postDto.title());
        existingPost.setContent(postDto.content());
        existingPost.setBlockComment(postDto.blockComment());
        existingPost.setImagePath(path);
        return existingPost;
    }

    public String deletePost(Long id) {
        User user = sessionService.getSession();
        if (!user.isEnabled()) throw new UserBlockedException();
        postRepository.deleteById(id);
        return "Post with ID: " + id + " deleted";
    }

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public Map<String, Object> getPost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with ID: " + postId));

        Map<String, Object> response = new HashMap<>();
        response.put("post", post);

        if (post.getImagePath() != null) response.put("imagePath", post.getImagePath());

        return response;
    }

    private String uploadFileToS3(MultipartFile file) {
        try {
            String key = "files/" + System.currentTimeMillis() + "-" + file.getOriginalFilename();
            return s3Service.uploadFile(key, file.getInputStream(), file.getSize(), file.getContentType());
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload file", e);
        }
    }


}
