package travel.persistence.dto;

import lombok.Data;

import java.util.List;

@Data
public class DestinationDto {
    private long id;
    private String name;
    private String country;
    private List<AttractionDto> attractions;
}
