package tmrv.dev.blogsystem.Services;


import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tmrv.dev.blogsystem.dtos.PostDto;
import tmrv.dev.blogsystem.entities.Post;
import tmrv.dev.blogsystem.entities.User;
import tmrv.dev.blogsystem.exception.ResourceNotFoundException;
import tmrv.dev.blogsystem.repository.PostRepository;
import tmrv.dev.blogsystem.repository.UserRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    private final String uploadDir = "src/main/resources/static/images/postImages/";

    public PostService(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;

        this.userRepository = userRepository;
    }


    public Post createPost(PostDto postDto, MultipartFile file) throws IOException {


        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Post post = new Post();
        post.setTitle(postDto.title());
        post.setContent(postDto.content());
        post.setPublished(postDto.published());
        post.setUser(user);

        return handleImage(file, post);
    }


    public Post updatePost(Long postId, PostDto postDto, MultipartFile file) throws IOException {
        Post existingPost = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (!existingPost.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("You are not authorized to update this post");
        }

        existingPost.setTitle(postDto.title());
        existingPost.setContent(postDto.content());
        existingPost.setPublished(postDto.published());

        return handleImage(file, existingPost);
    }

    public String deletePost(Long id) {
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

        if (post.getImagePath() != null) {
            response.put("imagePath", post.getImagePath());
        }

        return response;
    }

    private Post handleImage(MultipartFile file, Post existingPost) throws IOException {
        if (file != null && !file.isEmpty()) {
            String fileName = file.getOriginalFilename();
            Path filePath = Paths.get(uploadDir + fileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            existingPost.setImagePath("/images/postImages/" + fileName);
        }

        return postRepository.save(existingPost);
    }


}
