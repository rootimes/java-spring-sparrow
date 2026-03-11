package sparrow.post.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import sparrow.post.vo.enums.SourceType;

@Converter(autoApply = true)
public class SourceTypeConverter implements AttributeConverter<SourceType, Integer> {

    @Override
    public Integer convertToDatabaseColumn(SourceType attribute) {
        return attribute == null ? null : attribute.getValue();
    }

    @Override
    public SourceType convertToEntityAttribute(Integer dbData) {
        return SourceType.fromValue(dbData);
    }
}
