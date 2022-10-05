package travel.persistence;

import com.fasterxml.jackson.databind.ObjectMapper;
import travel.domain.*;
import travel.persistence.dto.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileDataStore implements DataStore {

    private static final String DESTINATIONS_FILE_NAME = "destinations.json";
    private static final String TRIPS_FILE_NAME = "trips.json";
    private static final String USERS_FILE_NAME = "users.json";
    private static final String REVIEWS_FILE_NAME = "reviews.json";

    private final String basePath;

    List<User> users;
    List<Trip> trips ;
    List<Review> reviews ;
    List<Destination> destinations;
    List<Attraction> attractions;

    public FileDataStore(String basePath) {
        this.basePath = basePath;
        users = new ArrayList<>();
        destinations = new ArrayList<>();
        trips = new ArrayList<>();
        attractions = new ArrayList<>();
        reviews = new ArrayList<>();
    }
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void loadData() {
        objectMapper.findAndRegisterModules();
        DestinationDto[] destinations = readDestinations();
        UserDto[] users = readUsers();
        TripDto[] trips = readTripss();
        ReviewDto[] reviews = readReaviews();

        ConvertUsersData(users);
        ConvertDestinationsData(destinations);
        ConvertReviewsData(reviews);
        ConvertTripsData(trips);
    }

    @Override
    public void saveData() {
        writeOutDestinations();
        writeOutReviews();
        writeOutTrips();
        writeOutUsers();
    }

    // region GETTERS
    private String getPath(String fileName) {
        return basePath + File.separator + fileName;
    }

    @Override
    public List<Destination> getDestinations() {
        return destinations;
    }

    @Override
    public List<User> getUsers() {
        return users;
    }

    @Override
    public List<Trip> getTrips() {
        return trips;
    }

    @Override
    public List<Review> getReviews() {
        return reviews;
    }
// endregion

    //region READ
    private DestinationDto[] readDestinations() {
        try {
            return objectMapper.readValue(new File(getPath(DESTINATIONS_FILE_NAME)), DestinationDto[].class);
        } catch (IOException e) {
            throw new RuntimeException("IO error happened while reading destinations: " + e.getMessage(), e);
        }
    }

    private TripDto[] readTripss() {
        try {
            return objectMapper.readValue(new File(getPath(TRIPS_FILE_NAME)), TripDto[].class);
        } catch (IOException e) {
            throw new RuntimeException("IO error happened while reading destinations: " + e.getMessage(), e);
        }
    }

    private ReviewDto[] readReaviews() {
        try {
            return objectMapper.readValue(new File(getPath(REVIEWS_FILE_NAME)), ReviewDto[].class);
        } catch (IOException e) {
            throw new RuntimeException("IO error happened while reading destinations: " + e.getMessage(), e);
        }
    }

    private UserDto[] readUsers() {
        try {
            return objectMapper.readValue(new File(getPath(USERS_FILE_NAME)), UserDto[].class);
        } catch (IOException e) {
            throw new RuntimeException("IO error happened while reading destinations: " + e.getMessage(), e);
        }
    }
// endregion

    // region JSON WRITE OUT
    private void writeOutDestinations(){
        try {
            objectMapper.writeValue(new File(getPath(DESTINATIONS_FILE_NAME)), DestinationDto[].class);
        } catch (IOException e) {
            throw new RuntimeException("IO error happened while writing destinations: " + e.getMessage(), e);
        }
    }
    private void writeOutReviews(){
        try {
            objectMapper.writeValue(new File(getPath(REVIEWS_FILE_NAME)), DestinationDto[].class);
        } catch (IOException e) {
            throw new RuntimeException("IO error happened while writing reviews: " + e.getMessage(), e);
        }
    }
    private void writeOutTrips(){
        try {
            objectMapper.writeValue(new File(getPath(TRIPS_FILE_NAME)), DestinationDto[].class);
        } catch (IOException e) {
            throw new RuntimeException("IO error happened while writing trips: " + e.getMessage(), e);
        }
    }
    private void writeOutUsers(){
        try {
            objectMapper.writeValue(new File(getPath(USERS_FILE_NAME)), DestinationDto[].class);
        } catch (IOException e) {
            throw new RuntimeException("IO error happened while writing users: " + e.getMessage(), e);
        }
    }

    //endregion

    // region CONVERT DATA
    private void ConvertReviewsData(ReviewDto[] dto){
        for (ReviewDto revD : dto) {
            Review newRev = new Review();
            newRev.setId(revD.getId());
            newRev.setRating(revD.getRating());
            newRev.setComment(revD.getComment());


            for (Destination des : destinations) {
                for (Attraction attr : des.getAttractions()) {
                    if (attr.getId()==revD.getAttractionId()){
                        newRev.setAttraction(attr);
                    }
                }
            }

            for (User u : users) {
                if (u.getId() == revD.getUserId()){
                    newRev.setUser(u);
                }
            }
            reviews.add(newRev);
        }
    }

    private List<Visit> ConvertVisitsData(List<VisitDto> dto){
        List<Visit> visits = new ArrayList<>();

        dto.forEach(visD -> {
            Visit visit = new Visit();
            visit.setId(visD.getId());
            visit.setVisitDate(visD.getVisitDate());
            destinations.forEach(des -> {
                des.getAttractions().forEach(atr -> {
                    if (atr.getId() == visD.getAttractionId()){
                        visit.setAttraction(atr);
                    }

                });
            });
            visits.add(visit);

        });

        /*
        for (VisitDto visD : dto) {
            Visit visit = new Visit();
            visit.setId(visD.getId());
            visit.setVisitDate(visD.getVisitDate());
            for (Destination des : destinations) {
                for (Attraction attr : des.getAttractions()) {
                    if (attr.getId()==visD.getAttractionId()){
                        visit.setAttraction(attr);
                    }
                }
            }
            visits.add(visit);
        }*/
        return visits;
    }

    private void setAttractions(){
        for (Destination des : destinations) {
            attractions.addAll(des.getAttractions());
        }
    }

    private void ConvertUsersData(UserDto[] dto){
        for (UserDto userD : dto) {
            User newU = new User();
            newU.setId(userD.getId());
            newU.setFullName(userD.getName());
            newU.setRole(userD.getRole());
            newU.setCredential(ConvertCredentialData(userD.getCredentials()));

            users.add(newU);
        }
    }

    private void ConvertTripsData(TripDto[] dto){
        for (TripDto tripD : dto) {
            Trip trip = new Trip();
            trip.setId(tripD.getId());
            trip.setStartDate(tripD.getStartDate());
            trip.setEndDate(tripD.getEndDate());

            for (User user : users) {
                if (user.getId() == tripD.getUserId()){
                    trip.setUser(user);
                }
            }

            for (Destination des : destinations) {
                if (des.getId() == tripD.getDestinationId()){
                    trip.setDestination(des);
                }
            }
            trip.setVisits(ConvertVisitsData(tripD.getVisits()));
            trips.add(trip);
        }
    }

    private Credentials ConvertCredentialData(CredentialsDto dto){
        Credentials cred = new Credentials();
        cred.setUsername(dto.getLoginName());
        cred.setPassword(dto.getPassword());

        return cred;
    }

    private void ConvertDestinationsData(DestinationDto[] dto){
        for (DestinationDto desD : dto) {
            Destination newDes = new Destination();
            newDes.setId(desD.getId());
            newDes.setName(desD.getName());
            newDes.setCountry(desD.getCountry());
            newDes.setAttractions(ConvertAttractionData(desD.getAttractions()));
            destinations.add(newDes);
        }
    }

    private List<Attraction> ConvertAttractionData(List<AttractionDto> dto){
        List<Attraction> attr = new ArrayList<>() {};
        dto.forEach( at -> {
            Attraction newAttr = new Attraction();
            newAttr.setId(at.getId());
            newAttr.setName(at.getName());
            newAttr.setDescription(at.getDescription());
            newAttr.setCategory(at.getCategory());
            attr.add(newAttr);
        });
        /*
        for (AttractionDto d : dto) {
            Attraction newAttr = new Attraction();
            newAttr.setId(d.getId());
            newAttr.setName(d.getName());
            newAttr.setDescription(d.getDescription());
            newAttr.setCategory(d.getCategory());
            attr.add(newAttr);
        }*/
        return attr;
    }
    // endregion

}

