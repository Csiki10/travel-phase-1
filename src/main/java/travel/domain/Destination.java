package travel.domain;

import java.util.List;
import lombok.Data;
@Data
public class Destination {

    private long id;
    private String name;
    private String country;
    private List<Attraction> attractions;
}
