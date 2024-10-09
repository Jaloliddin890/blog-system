package tmrv.dev.blogsystem.Services;


import org.springframework.stereotype.Service;
import tmrv.dev.blogsystem.dtos.PostDto;
import tmrv.dev.blogsystem.entities.Post;
import tmrv.dev.blogsystem.entities.User;
import tmrv.dev.blogsystem.repository.PostRepository;

@Service
public class PostService {

    private final PostRepository postRepository;
    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public Post createPost(PostDto postDto, User user) {
        Post post = new Post();
        post.setTitle(postDto.title());
        post.setContent(postDto.content());
        post.setPublished(postDto.published());
        post.setUser(user);
        return postRepository.save(post);
    }
}
