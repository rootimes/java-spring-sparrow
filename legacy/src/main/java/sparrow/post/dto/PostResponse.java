package sparrow.post.dto;

import java.util.Set;

import sparrow.post.vo.Meta;
import sparrow.post.vo.enums.PostStatus;

public class PostResponse {
    private Integer id;
    private Integer userId;
    private String title;
    private String slug;
    private Meta meta;
    private PostStatus status;
    private PostSourceResponse source;

    private Set<String> tags;

    public PostResponse() {
    }

    public PostResponse(Integer id, Integer userId, String title, String slug, Meta meta, PostStatus status) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.slug = slug;
        this.meta = meta;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public PostSourceResponse getSource() {
        return source;
    }

    public void setSource(PostSourceResponse source) {
        this.source = source;
    }

    public Set<String> getTags() {
        return tags;
    }

    public void setTags(Set<String> tags) {
        this.tags = tags;
    }
}
