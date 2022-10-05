package travel.persistence.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class VisitDto {
    private long id;
    private LocalDate visitDate;
    private long attractionId;
}
