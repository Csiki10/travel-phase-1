package travel.service;

import travel.domain.*;
import travel.persistence.DataStore;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class DefaultTravelService implements TravelService {

    private DataStore dataStore;
    private User loggedinUser;

    public DefaultTravelService(DataStore dataStore) {
        this.dataStore = dataStore;
        this.loggedinUser = new User();
    }
    @Override
    public User authenticateUser(Credentials credentials) {
        for (User user : dataStore.getUsers()) {
            if (user.getCredential().equals(credentials) ) {
                loggedinUser = user;
                return user;
            }
        }
        return null;
    }

    @Override
    public List<Trip> getTrips(LocalDate startDate, LocalDate endDate) {
        return dataStore.getTrips()
                .stream()
                .filter(t->t.getUser() == loggedinUser
                        && (t.getEndDate().isBefore(endDate) || endDate.isEqual(t.getEndDate()))
                        && (t.getStartDate().isAfter(startDate) || startDate.isEqual(t.getStartDate()))
                )
                .collect(Collectors.toList());
    }

    @Override
    public Statistics getStatistics() {
        Statistics s = new Statistics();
        s.setNumberOfUsers(dataStore.getUsers().size());
        s.setNumberOfAllReviews(dataStore.getReviews().size());
        s.setNumberOfDestinations(dataStore.getDestinations().size());
        int vis = 0;
        for (Trip t : dataStore.getTrips()) {
            if (t.getUser() == loggedinUser){
                for (Visit v : t.getVisits()) {
                    vis++;
                }
            }
        }
        s.setNumberOfUserVisits(vis);
        s.setNumberOfUserWrittenReviews(
                ((int) dataStore.getReviews()
                        .stream()
                        .filter(t -> t.getUser() == loggedinUser)
                        .count()));
        int db = 0;
        for (Destination des : dataStore.getDestinations()) {
            db += des.getAttractions().size();
        }
        s.setNumberOfAttractions(db);

        return s;
    }

    @Override
    public List<Destination> getDestinations() {
        return dataStore.getDestinations();
    }

    @Override
    public List<Review> getReviews(long attractionId) {
        return dataStore.getReviews()
                .stream()
                .filter(t->t.getAttraction().getId() == attractionId)
                .collect(Collectors.toList());
    }

    @Override
    public void createAttraction(long destinationId, Attraction attraction) {
        dataStore.getDestinations()
                .stream()
                .filter(t->t.getId()==destinationId)
                .findFirst().get().getAttractions().add(attraction);
    }

    @Override
    public void createTrip(Trip trip) {
        dataStore.getTrips().add(trip);
    }

    @Override
    public List<Attraction> getAttractions() {
        return dataStore.getAttractions();
    }

    @Override
    public long getNextTripId() {
        var a = dataStore.getTrips().get(dataStore.getTrips().size()-1).getId();
        return dataStore.getTrips().get(dataStore.getTrips().size()-1).getId() +1 ;
    }

    @Override
    public long getNextAttractionId() {
        var a = dataStore.getAttractions().get(dataStore.getAttractions().size()-1).getId();
        return dataStore.getAttractions().get(dataStore.getAttractions().size()-1).getId() +1 ;
    }
}