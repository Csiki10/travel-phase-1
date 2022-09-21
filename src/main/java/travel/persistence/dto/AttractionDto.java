package travel.persistence.dto;

import lombok.Data;
import travel.domain.Category;
@Data
public class AttractionDto {
    private long id;
    private String name;
    private String description;
    private Category category;
}
