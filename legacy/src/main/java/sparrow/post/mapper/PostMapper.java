package sparrow.post.mapper;

import org.springframework.stereotype.Component;

import sparrow.post.dto.PostListResponse;
import sparrow.post.dto.PostRequest;
import sparrow.post.dto.PostResponse;
import sparrow.post.dto.PostSourceRequest;
import sparrow.post.entity.Post;
import sparrow.post.entity.PostSource;

@Component
public class PostMapper {

    private final PostSourceMapper postSourceMapper;

    public PostMapper(PostSourceMapper postSourceMapper) {
        this.postSourceMapper = postSourceMapper;
    }

    public PostListResponse toListResponse(Post entity) {
        if (entity == null) {
            return null;
        }
        PostListResponse response = new PostListResponse();
        response.setId(entity.getId());
        response.setUserId(entity.getUserId());
        response.setTitle(entity.getTitle());
        response.setSlug(entity.getSlug());
        response.setMeta(entity.getMeta());
        response.setStatus(entity.getStatus());
        return response;
    }

    public PostResponse toResponse(Post entity) {
        if (entity == null) {
            return null;
        }
        PostResponse response = new PostResponse();
        response.setId(entity.getId());
        response.setUserId(entity.getUserId());
        response.setTitle(entity.getTitle());
        response.setSlug(entity.getSlug());
        response.setMeta(entity.getMeta());
        response.setStatus(entity.getStatus());
        response.setSource(postSourceMapper.toResponse(entity.getPostSource()));
        response.setTags(entity.getTagNames());
        return response;
    }

    public void updateEntity(PostRequest request, Post entity) {
        if (request == null) {
            return;
        }
        if (request.getTitle() != null) {
            entity.setTitle(request.getTitle());
        }
        if (request.getSlug() != null) {
            entity.setSlug(request.getSlug());
        }
        if (request.getMeta() != null) {
            entity.setMeta(request.getMeta());
        }
        if (request.getStatus() != null) {
            entity.setStatus(request.getStatus());
        }
        PostSourceRequest sourceReq = request.getSource();
        if (sourceReq != null) {
            PostSource postSource = entity.getPostSource();
            if (postSource == null) {
                postSource = new PostSource();
                entity.updateSource(postSource);
            }
            postSourceMapper.updateEntity(sourceReq, postSource);
        }
    }
}
