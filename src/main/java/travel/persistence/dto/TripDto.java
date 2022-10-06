package travel.persistence.dto;


import travel.domain.Destination;
import travel.domain.User;

import java.time.LocalDate;
import java.util.List;
import lombok.Data;
@Data
public class TripDto {

    private long id;
    private long userId;
    private long destinationId;
    private LocalDate startDate;
    private LocalDate endDate;
    private List<VisitDto> visits;
}
