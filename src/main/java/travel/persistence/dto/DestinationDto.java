package travel.persistence.dto;
import java.util.List;
import lombok.Data;
@Data
public class DestinationDto {
    private long id;
    private String name;
    private String country;
    private List<AttractionDto> attractions;
}
