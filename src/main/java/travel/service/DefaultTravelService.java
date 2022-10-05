package travel.service;

import travel.domain.*;
import travel.persistence.DataStore;

import java.time.LocalDate;
import java.util.ArrayList;
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
        for (User u : dataStore.getUsers()) {
            if (u.getCredential() == credentials) {
                loggedinUser = u;
                return u;
            }
        }
        throw new AuthenticationException("Invalid user credentials");
    }

    @Override
    public Statistics getStatistics() {
        Statistics stat = new Statistics();
        stat.setNumberOfUsers(dataStore.getUsers().size());
        stat.setNumberOfAllReviews(dataStore.getReviews().size());
        stat.setNumberOfDestinations(dataStore.getDestinations().size());

        int attrNum = 0;
        for (Destination destination : dataStore.getDestinations()) {
            attrNum += destination.getAttractions().size();
        }
        stat.setNumberOfAttractions(attrNum);

        int VisitsNum = 0;
        for (Trip trip : dataStore.getTrips()) {
            if (trip.getUser() == loggedinUser){
                for (Visit v : trip.getVisits()) {
                    VisitsNum++;
                }
            }
        }
        stat.setNumberOfUserVisits(VisitsNum);

        /*int writtenRev = 0;
        for (Review rev : dataStore.getReviews()) {
            if (rev.getUser() == loggedinUser){
                writtenRev++;
            }
        }*/
        stat.setNumberOfUserWrittenReviews(((int) dataStore.getReviews().stream().filter(t -> t.getUser() == loggedinUser).count()));
        return stat;
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
        dataStore.getDestinations().stream().filter(t->t.getId()==destinationId).findFirst().get().getAttractions().add(attraction);
    }

    @Override
    public List<Trip> getTrips(LocalDate startDate, LocalDate endDate) {
        return dataStore.getTrips()
                .stream()
                .filter(t->t.getUser() == loggedinUser
                        && (startDate.isAfter(t.getStartDate()) || startDate.isEqual(t.getStartDate()))
                        && (endDate.isBefore(t.getEndDate()) || endDate.isEqual(t.getEndDate())))
                .collect(Collectors.toList());
    }

    @Override
    public void createTrip(Trip trip) {
        dataStore.getTrips().add(trip);
    }
}
