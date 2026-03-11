package sparrow.post.entity;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import sparrow.post.vo.SourceLink;
import sparrow.post.vo.enums.SourceType;

@Entity
@Table(name = "post_sources")
public class PostSource {
    @Id
    @Column(name = "post_id")
    private Integer postId;

    @OneToOne(optional = false)
    @MapsId
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @Column(name = "source_type", nullable = false)
    private SourceType sourceType;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "source_link", columnDefinition = "json")
    private SourceLink sourceLink;

    public PostSource() {
    }

    public Integer getPostId() {
        return postId;
    }

    public void setPostId(Integer postId) {
        this.postId = postId;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public SourceType getSourceType() {
        return sourceType;
    }

    public void setSourceType(SourceType sourceType) {
        this.sourceType = sourceType;
    }

    public SourceLink getSourceLink() {
        return sourceLink;
    }

    public void setSourceLink(SourceLink sourceLink) {
        this.sourceLink = sourceLink;
    }
}
