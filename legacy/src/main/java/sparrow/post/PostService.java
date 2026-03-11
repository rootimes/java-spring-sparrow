package sparrow.post;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sparrow.exception.ConflictException;
import sparrow.exception.ResourceNotFoundException;
import sparrow.post.dto.PostListResponse;
import sparrow.post.dto.PostRequest;
import sparrow.post.dto.PostResponse;
import sparrow.post.entity.Post;
import sparrow.post.entity.Tag;
import sparrow.post.mapper.PostMapper;
import sparrow.post.repository.PostRepository;
import sparrow.post.repository.TagRepository;
import sparrow.post.vo.Meta;
import sparrow.user.User;
import sparrow.user.UserRepository;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final PostMapper postMapper;
    private final TagRepository tagRepository;

    public PostService(
            PostRepository postRepository,
            UserRepository userRepository,
            PostMapper postMapper,
            TagRepository tagRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.postMapper = postMapper;
        this.tagRepository = tagRepository;
    }

    @Transactional(readOnly = true)
    public Page<PostListResponse> getAllPosts(Pageable pageable) {
        return postRepository.findAllForList(pageable)
                .map(postMapper::toListResponse);
    }

    @Transactional(readOnly = true)
    public PostResponse getPostById(int id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));
        return postMapper.toResponse(post);
    }

    @Transactional(readOnly = true)
    public PostResponse getPostBySlug(String slug) {
        Post post = postRepository.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found by slug"));
        return postMapper.toResponse(post);
    }

    @Transactional
    public PostResponse createPost(PostRequest request) {
        if (request.getSlug() != null && postRepository.existsBySlug(request.getSlug())) {
            throw new ConflictException("Slug already exists: " + request.getSlug());
        }

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Post post = new Post();
        postMapper.updateEntity(request, post);
        post.setUser(user);
        applyTags(post, request.getTags());

        Post savedPost = postRepository.save(post);
        return postMapper.toResponse(savedPost);
    }

    @Transactional
    public PostResponse updatePost(int id, PostRequest request) {
        Post existingPost = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));

        if (request.getSlug() != null && postRepository.existsBySlugAndIdNot(request.getSlug(), id)) {
            throw new ConflictException("Slug already exists: " + request.getSlug());
        }

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        postMapper.updateEntity(request, existingPost);
        existingPost.setUser(user);
        applyTags(existingPost, request.getTags());

        Post updatedPost = postRepository.save(existingPost);
        return postMapper.toResponse(updatedPost);
    }

    @Transactional
    public void deletePost(int id) {
        if (!postRepository.existsById(id)) {
            throw new ResourceNotFoundException("Post not found");
        }
        postRepository.deleteById(id);
    }

    private void applyTags(Post post, Set<String> tagNames) {
        post.clearTags();
        List<String> tagList = new ArrayList<>();
        if (tagNames != null && !tagNames.isEmpty()) {
            for (String name : tagNames) {
                Tag tag = findOrCreateTag(name);
                post.addTag(tag);
                tagList.add(name);
            }
        }

        updateMetaForTags(post, tagList);
    }

    private Tag findOrCreateTag(String name) {
        return tagRepository.findByName(name)
                .orElseGet(() -> {
                    Tag newTag = new Tag();
                    newTag.setName(name);
                    return tagRepository.save(newTag);
                });
    }

    private void updateMetaForTags(Post post, List<String> tagList) {
        Meta meta = post.getMeta();
        if (meta == null) {
            meta = new Meta();
            post.setMeta(meta);
        }
        meta.setTags(tagList);
    }
}
