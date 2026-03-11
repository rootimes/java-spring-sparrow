package sparrow.post.mapper;

import org.springframework.stereotype.Component;

import sparrow.post.dto.PostSourceRequest;
import sparrow.post.dto.PostSourceResponse;
import sparrow.post.entity.PostSource;

@Component
public class PostSourceMapper {

    public PostSourceResponse toResponse(PostSource entity) {
        if (entity == null) {
            return null;
        }
        PostSourceResponse response = new PostSourceResponse();
        response.setSourceType(entity.getSourceType());
        response.setSourceLink(entity.getSourceLink());
        return response;
    }

    public void updateEntity(PostSourceRequest request, PostSource entity) {
        if (request == null) {
            return;
        }
        if (request.getSourceType() != null) {
            entity.setSourceType(request.getSourceType());
        }
        if (request.getSourceLink() != null) {
            entity.setSourceLink(request.getSourceLink());
        }
    }
}
