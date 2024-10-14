package tmrv.dev.blogsystem.Services;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import tmrv.dev.blogsystem.dtos.PostDto;
import tmrv.dev.blogsystem.entities.Post;
import tmrv.dev.blogsystem.entities.User;
import tmrv.dev.blogsystem.exception.ResourceNotFoundException;
import tmrv.dev.blogsystem.repository.PostRepository;
import tmrv.dev.blogsystem.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PostService {

    private final PostRepository postRepository;

    private final UserRepository userRepository;

    public PostService(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }


    public Post createPost(PostDto postDto) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        Post post = new Post();
        post.setTitle(postDto.title());
        post.setContent(postDto.content());
        post.setPublished(postDto.published());
        post.setUser(user);
        return postRepository.save(post);
    }

    public Post updatePost(PostDto postDto, Long id) {
        return postRepository.findById(id).map(post -> {
            post.setTitle(postDto.title());
            post.setContent(postDto.content());
            post.setPublished(postDto.published());

            post.setUpdatedAt(LocalDateTime.now());

            return postRepository.save(post);
        }).orElseThrow(() -> new ResourceNotFoundException("Post not found with ID: " + id));
    }

    public String deletePost(Long id) {
        postRepository.deleteById(id);
        return "Post with ID: " + id + " deleted";
    }

    public Page<Post> getPosts(Pageable pageable) {
        return postRepository.findAll(pageable);
    }

}
