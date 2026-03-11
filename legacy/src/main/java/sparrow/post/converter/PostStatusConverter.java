package sparrow.post.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import sparrow.post.vo.enums.PostStatus;

@Converter(autoApply = true)
public class PostStatusConverter implements AttributeConverter<PostStatus, Integer> {

    @Override
    public Integer convertToDatabaseColumn(PostStatus attribute) {
        return attribute == null ? null : attribute.getValue();
    }

    @Override
    public PostStatus convertToEntityAttribute(Integer dbData) {
        return PostStatus.fromValue(dbData);
    }
}
