package travel.persistence;

import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.datatype:jackson-datatype-jsr310
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializationFeature;
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

    private static final String DESTINATIONS_FILE_NAME1 = "destinations1.json";
    private static final String TRIPS_FILE_NAME1 = "trips1.json";
    private static final String USERS_FILE_NAME1 = "users1.json";
    private static final String REVIEWS_FILE_NAME1 = "reviews1.json";
    private final String basePath;

    List<User> users;
    List<Trip> trips ;
    List<Review> reviews ;
    List<Destination> destinations;
    List<Attraction> attractions;

    DestinationDto[] destinationsDtos;
    UserDto[] usersDtos ;
    TripDto[] tripsDtos  ;
    ReviewDto[] reviewsDtos ;

    List<DestinationDto> destinationsDtosBack;
    List<UserDto> usersDtosBack ;
    List<TripDto> tripsDtosBack ;
    List<ReviewDto> reviewsDtosBack;

    public FileDataStore(String basePath) {
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
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
        destinationsDtos = readDestinations();
        usersDtos = readUsers();
        tripsDtos = readTrips();
        reviewsDtos = readReviews();

        ConvertUsersData(usersDtos);
        ConvertDestinationsData(destinationsDtos);
        ConvertReviewsData(reviewsDtos);
        ConvertTripsData(tripsDtos);
        setAttractions();
    }

    @Override
    public void saveData() {
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

        destinationsDtosBack = new ArrayList<>();
        reviewsDtosBack = new ArrayList<>();
        tripsDtosBack = new ArrayList<>();
        usersDtosBack = new ArrayList<>();


        ConvertBackReviewsData();
        ConvertBackUsersData();
        ConvertBackTripsData();
        ConvertBackDestinationsData();

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

    public List<Attraction> getAttractions() {
        return attractions;
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

    private TripDto[] readTrips() {
        try {
            return objectMapper.readValue(new File(getPath(TRIPS_FILE_NAME)), TripDto[].class);
        } catch (IOException e) {
            throw new RuntimeException("IO error happened while reading destinations: " + e.getMessage(), e);
        }
    }

    private ReviewDto[] readReviews() {
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
            objectMapper.writeValue(new File(getPath(DESTINATIONS_FILE_NAME)), destinationsDtosBack);
        } catch (IOException e) {
            throw new RuntimeException("IO error happened while writing destinations: " + e.getMessage(), e);
        }
    }
    private void writeOutReviews(){
        try {
            objectMapper.writeValue(new File(getPath(REVIEWS_FILE_NAME)), reviewsDtosBack);
        } catch (IOException e) {
            throw new RuntimeException("IO error happened while writing reviews: " + e.getMessage(), e);
        }
    }
    private void writeOutTrips(){
        try {
            objectMapper.writeValue(new File(getPath(TRIPS_FILE_NAME)), tripsDtosBack);
        } catch (IOException e) {
            throw new RuntimeException("IO error happened while writing trips: " + e.getMessage(), e);
        }
    }
    private void writeOutUsers(){
        try {
            objectMapper.writeValue(new File(getPath(USERS_FILE_NAME)), usersDtosBack);
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
    private void ConvertBackReviewsData(){
        reviews.forEach(r -> {
            ReviewDto rdto = new ReviewDto();

            rdto.setId(r.getId());
            rdto.setUserId((int)r.getUser().getId());
            rdto.setRating(r.getRating());
            rdto.setComment(r.getComment());
            rdto.setAttractionId(r.getAttraction().getId());

            reviewsDtosBack.add(rdto);
        });
    }

    private void ConvertUsersData(UserDto[] dto){
        for (UserDto userD : dto) {
            User newU = new User();
            newU.setId(userD.getId());
            newU.setFullName(userD.getName());
            newU.setRole(userD.getRole());
            newU.setEmail(userD.getEmail());
            newU.setCredential(ConvertCredentialData(userD.getCredentials()));

            users.add(newU);
        }
    }
    private void ConvertBackUsersData(){
        users.forEach(u -> {
            UserDto udto = new UserDto();

            udto.setId(u.getId());
            udto.setName(u.getFullName());
            udto.setEmail(u.getEmail());
            udto.setRole(u.getRole());
            udto.setEmail(u.getEmail());

            CredentialsDto c = new CredentialsDto();
            c.setPassword(u.getCredential().getPassword());
            c.setLoginName(u.getCredential().getUsername());

            udto.setCredentials(c);

            usersDtosBack.add(udto);
        });
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
    private void ConvertBackTripsData(){
        trips.forEach(t -> {
            TripDto tdto = new TripDto();

            tdto.setUserId(t.getUser().getId());
            tdto.setId(t.getId());
            tdto.setDestinationId(t.getDestination().getId());
            tdto.setStartDate(t.getStartDate());
            tdto.setEndDate(t.getEndDate());

            List<VisitDto> vdto = new ArrayList<>();
            t.getVisits().forEach(v -> {
                VisitDto v1 = new VisitDto();
                v1.setAttractionId(v.getAttraction().getId());
                v1.setVisitDate(v.getVisitDate());
                v1.setId(v.getId());
                vdto.add(v1);
            });

            tdto.setVisits(vdto);
            tripsDtosBack.add(tdto);
        });
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
    private void ConvertBackDestinationsData(){
        destinations.forEach(d ->{
            DestinationDto des  = new DestinationDto();

            des.setName(d.getName());
            des.setId(d.getId());
            des.setCountry(d.getCountry());

            List<AttractionDto> atrrs = new ArrayList<>();
            attractions.forEach(a -> {
                AttractionDto adto = new AttractionDto();
                adto.setName(a.getName());
                adto.setCategory(a.getCategory());
                adto.setDescription(a.getDescription());
                adto.setId(a.getId());
                atrrs.add(adto);
            });
            des.setAttractions(atrrs);

            destinationsDtosBack.add(des);
        });
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
        return visits;
    }
    private Credentials ConvertCredentialData(CredentialsDto dto){
        Credentials cred = new Credentials();
        cred.setUsername(dto.getLoginName());
        cred.setPassword(dto.getPassword());

        return cred;
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

        return attr;
    }
    private void setAttractions(){
        for (Destination des : destinations) {
            attractions.addAll(des.getAttractions());
        }
    }

    // endregion

}

