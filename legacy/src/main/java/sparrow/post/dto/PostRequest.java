package sparrow.post.dto;

import java.util.Set;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import sparrow.post.vo.Meta;
import sparrow.post.vo.enums.PostStatus;

public class PostRequest {
    @NotNull(message = "userId is required")
    private Integer userId;

    @NotBlank(message = "title is required")
    @Size(max = 100, message = "title max length is 100")
    private String title;

    @Size(max = 100, message = "slug max length is 100")
    private String slug;

    private Meta meta;

    private PostStatus status;

    private PostSourceRequest source;

    private Set<String> tags;

    public PostRequest() {
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public PostStatus getStatus() {
        return status;
    }

    public void setStatus(PostStatus status) {
        this.status = status;
    }

    public PostSourceRequest getSource() {
        return source;
    }

    public void setSource(PostSourceRequest source) {
        this.source = source;
    }

    public Set<String> getTags() {
        return tags;
    }

    public void setTags(Set<String> tags) {
        this.tags = tags;
    }
}
