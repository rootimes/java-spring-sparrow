package sparrow.post;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.ResponseStatus;

import jakarta.validation.Valid;
import sparrow.post.dto.PostListResponse;
import sparrow.post.dto.PostRequest;
import sparrow.post.dto.PostResponse;

@RestController
@RequestMapping("/api/posts")
public class PostController {
    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    public Page<PostListResponse> getAllPosts(Pageable pageable) {
        return postService.getAllPosts(pageable);
    }

    @GetMapping("/{id:\\d+}")
    public PostResponse getPostById(@PathVariable("id") int id) {
        return postService.getPostById(id);
    }

    @GetMapping("/{slug:[a-z0-9-]*[a-z][a-z0-9-]*}")
    public PostResponse getPostBySlug(@PathVariable("slug") String slug) {
        return postService.getPostBySlug(slug);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PostResponse createPost(@Valid @RequestBody PostRequest request) {
        return postService.createPost(request);
    }

    @PutMapping("/{id:\\d+}")
    public PostResponse updatePost(
            @PathVariable("id") int id,
            @Valid @RequestBody PostRequest request) {
        return postService.updatePost(id, request);
    }

    @DeleteMapping("/{id:\\d+}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePost(@PathVariable("id") int id) {
        postService.deletePost(id);
    }
}
