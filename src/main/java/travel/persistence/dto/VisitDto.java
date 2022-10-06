package travel.persistence.dto;
import java.time.LocalDate;
import lombok.Data;

@Data
public class VisitDto {
    private long id;
    private LocalDate visitDate;
    private long attractionId;
}
