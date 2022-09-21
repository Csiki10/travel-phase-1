package travel.persistence;

import com.fasterxml.jackson.databind.ObjectMapper;
import travel.persistence.dto.DestinationDto;

import java.io.File;
import java.io.IOException;

public class FileDataStore implements DataStore {

    private static final String DESTINATIONS_FILE_NAME = "destinations.json";
    private final String basePath;

    public FileDataStore(String basePath) {
        this.basePath = basePath;
    }
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void loadData() {
        DestinationDto[] destinations = readDestinations();
    }

    private DestinationDto[] readDestinations() {
        try {
            return objectMapper.readValue(new File(getPath(DESTINATIONS_FILE_NAME)), DestinationDto[].class);
        } catch (IOException e) {
            throw new RuntimeException("IO error happened while reading destinations: " + e.getMessage(), e);
        }
    }

    private String getPath(String fileName) {
        return basePath + File.separator + fileName;
    }
}
