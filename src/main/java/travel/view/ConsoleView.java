package travel.view;
import travel.View;
import travel.domain.*;
import travel.service.TravelService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.List;
import java.lang.String;


public class ConsoleView implements View {
    Scanner scanner;
    TravelService travelService;
    User u;

    public ConsoleView(TravelService travelService) {
        this.travelService = travelService;
        this.scanner = new Scanner(System.in);
    }

    public void Run(){
        System.out.println("Travel Application Login");
        System.out.println("Please enter your credentials.");
        System.out.print("Name:");
        String name = scanner.nextLine();
        System.out.print("Password:");
        String pass = scanner.nextLine();
        Credentials c = new Credentials(name,pass);

        u = travelService.authenticateUser(c);
        if (u != null){
            System.out.println("Login succesful!");
            Statistic();
        }
        else
            System.out.print("Login failure, bye.");
    }
    private void MenuAdmin(){
        Boolean go = true;
        do {
            System.out.println();
            System.out.println("Travel Application Menu");
            System.out.println("1. View Destinations and Attractions");
            System.out.println("2. View Reviews");
            System.out.println("3. View my Trips");
            System.out.println("4.Create Trip");
            System.out.println("5. Add Attraction");
            System.out.println("6. Quit Application");

            System.out.print("Please choose an item: ");
            int opt = scanner.nextInt();

            switch (opt) {
                case 1:
                    Case1Menu();
                    break;
                case 2:
                    Case2Menu();
                    break;
                case 3:
                    Case3Menu();
                    break;
                case 4:
                    Case4Menu();
                    break;
                case 5:
                    Case5MenuAdmin();
                    break;
                case 6:
                    travelService.saveData();
                    System.exit(0);

                    break;
                default:
                    System.out.println("Invalid menu item id");
            }

        }while(go);

    }
    private void MenuUser(){

        Boolean go = true;
        do {
            System.out.println();
            System.out.println("Travel Application Menu");
            System.out.println("1. View Destinations and Attractions");
            System.out.println("2. View Reviews");
            System.out.println("3. View my Trips");
            System.out.println("4. Create Trip");
            System.out.println("5. Quit Application");

            System.out.print("Please choose an item: ");
            int opt = scanner.nextInt();

            switch (opt) {
                case 1:
                    Case1Menu();
                    break;
                case 2:
                    Case2Menu();
                    break;
                case 3:
                    Case3Menu();
                    break;
                case 4:
                    Case4Menu();
                    break;
                case 5:
                    travelService.saveData();
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid menu item id");
            }

        }while(go);

    }

    private void Case1Menu() {
        System.out.println("Destinations and Attraction details:");
        List<Destination> des = travelService.getDestinations();
        des.forEach(d -> {
            System.out.println("- "+d.getName()+", "+ d.getId());
            d.getAttractions().forEach(a ->{
                System.out.println("\t - name: "+a.getName());
                System.out.println("\t - description: "+a.getDescription());
                System.out.println("\t - category: "+a.getCategory());
            });
        });

        scanner.nextLine();
        if (u.getRole().equals(Role.USER)){
            MenuUser();
        }
        else{
            MenuAdmin();
        }

    }
    private void Case2Menu(){
        System.out.println("List of Attractions:");
        travelService.getAttractions()
                .forEach(a -> {
                    System.out.println(a.getId()+". "+a.getName());
                });

        System.out.print("Please select:");
        int res = scanner.nextInt();

        String at = travelService.getAttractions()
                .stream()
                .filter(a -> a.getId() == res)
                .findFirst().get().getName();
        System.out.println("Reviews for: "+at);

        travelService.getReviews(res)
                .forEach(r -> {
                    System.out.println("- reviewer: "+ r.getUser().getFullName());
                    System.out.println("  rating: "+ r.getRating());
                    System.out.println("  comment: "+ r.getComment());
                });

        scanner.nextLine();
        if (u.getRole().equals(Role.USER)){
            MenuUser();
        }
        else{
            MenuAdmin();
        }
    }
    private void Case3Menu(){

        Boolean go = true;
        do {
            LocalDate start;
            LocalDate end;
            try{
                System.out.println("Query Trips");

                System.out.print("Enter start date:");
                start = LocalDate.parse(scanner.nextLine());

                System.out.print("Enter end date:");
                end = LocalDate.parse(scanner.nextLine());


                if (end.compareTo(start) > 0){
                    List<Trip> trips = travelService.getTrips(start,end);
                    if (trips.size() > 0){
                        System.out.println("List of Visits in the given range");
                        trips.forEach(t -> {
                            System.out.println("- "+t.getDestination().getName()
                                    +" ["+t.getStartDate()+";"+t.getEndDate()+"]");
                        });
                        go = false;
                    }
                    else{
                        System.out.println("Trip not found in the given date range.");
                    }
                }
                else{
                    System.out.println("Start date must not be before end date.");
                }
            }
            catch (Exception e){
                System.out.println("Entered date is invalid. Correct date format: YYYY-MM-DD");
                System.out.println(e.getMessage());
            }
        }while(go);

        String b = scanner.nextLine();
        if (u.getRole().equals(Role.USER)){
            MenuUser();
        }
        else{
            MenuAdmin();
        }
    }
    private void Case4Menu(){
        System.out.println("Create Trip");
        System.out.println("Destinations");
        travelService.getDestinations().forEach(d -> {
            System.out.println(d.getId()+". "+d.getName());
        });
        System.out.print("Please choose destination to create Trip:");
        int id = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter trip start date:");
        LocalDate start = LocalDate.parse(scanner.nextLine());
        System.out.print("Enter trip end date:");
        LocalDate end = LocalDate.parse(scanner.nextLine());
        scanner.nextLine();

        var des = travelService.getDestinations().stream().filter(d -> d.getId() == id).findFirst().get();
        List<Visit> v = new ArrayList<>();
        long idx = travelService.getNextTripId();
        Trip t = new Trip(idx,start,end,u,des,v);
        travelService.createTrip(t);
        System.out.print("Trip has been created.");
        if (u.getRole().equals(Role.USER)){
            MenuUser();
        }
        else{
            MenuAdmin();
        }
    }
    private void Case5MenuAdmin(){
        System.out.println("List of Destinations:");
        travelService.getDestinations().forEach(a -> {
            System.out.println(a.getId()+". "+a.getName());
        });
        System.out.print("Please select:");
        int id = scanner.nextInt();

        System.out.println("Please enter new attraction details");
        scanner.nextLine();
        System.out.print("name:");
        String name = scanner.nextLine();
        System.out.print("description:");
        String des = scanner.nextLine();

        Category c = null;
        Boolean go = true;
        do {
            System.out.print("category:");
            String newCat = scanner.nextLine();
            Category[] cats = Category.values();
            for(Category cat: cats) {
                if (cat.name().equals(newCat)){
                    go = false;
                    c = Category.valueOf(newCat);
                }
            }
            if (go){
                System.out.println("Unknown category");
            }
        }while(go);

        Attraction a = new Attraction(travelService.getNextAttractionId(),name,des,c);
        travelService.getAttractions().add(a);
        travelService.getDestinations().stream().filter(d -> d.getId() == id).findFirst().get().getAttractions().add(a);
        System.out.println("Attraction added.");
        MenuAdmin();
    }

    private void Statistic(){
        if (u.getRole() == Role.ADMIN){
            StaticticAdmin();
            MenuAdmin();
        }
        else {
            StaticticUser();
            MenuUser();
        }

    }
    private void StaticticUser(){
        System.out.println("Welcome"+u.getFullName()+". Your role: "+u.getRole());
        System.out.println("Application statistics:");
        System.out.println("Number of destinations:" + travelService.getStatistics().getNumberOfDestinations());
        System.out.println("Number of attractions:" + travelService.getStatistics().getNumberOfAttractions());
        System.out.println("Number of users:" + travelService.getStatistics().getNumberOfUsers());
        System.out.println("Number of reviews:" + travelService.getStatistics().getNumberOfAllReviews());
        System.out.println("Number of your visits:" + travelService.getStatistics().getNumberOfUserVisits());
        System.out.println("Number of reviews:" + travelService.getStatistics().getNumberOfUserWrittenReviews());
    }
    private void StaticticAdmin(){
        System.out.println("Welcome"+u.getFullName()+". Your role: "+u.getRole());
        System.out.println("Application statistics:");
        System.out.println("Number of destinations:" + travelService.getStatistics().getNumberOfDestinations());
        System.out.println("Number of attractions:" + travelService.getStatistics().getNumberOfAttractions());
        System.out.println("Number of users:" + travelService.getStatistics().getNumberOfUsers());
        System.out.println("Number of reviews:" + travelService.getStatistics().getNumberOfAllReviews());
    }
}

