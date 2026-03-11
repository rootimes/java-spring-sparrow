package sparrow.post.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import sparrow.post.vo.SourceLink;
import sparrow.post.vo.enums.SourceType;

public class PostSourceResponse {
    @JsonProperty("source_type")
    private SourceType sourceType;

    @JsonProperty("source_link")
    private SourceLink sourceLink;

    public PostSourceResponse() {}

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
