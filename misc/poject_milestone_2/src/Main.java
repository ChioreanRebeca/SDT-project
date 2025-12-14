import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // ----- OBSERVER setup -----
        EventBus eventBus = new EventBus();
        eventBus.subscribe(EventType.REVIEW_POSTED, new NotificationService());
        eventBus.subscribe(EventType.REVIEW_POSTED, new AnalyticsService());
        eventBus.subscribe(EventType.REVIEW_POSTED, new ReputationService());

        ReviewService reviewService = new ReviewService(eventBus);

        // ----- ADAPTER setup -----
        MapService mapService = new GoogleMapsAdapter(new ExternalGoogleMapsAPI());

        // ----- BUILDER: create some businesses -----
        System.out.println("=== BUILDER: create business profiles ===");

        BusinessProfile pizza = new BusinessProfileBuilder()
                .withId(1L)
                .withName("Mario Pizza")
                .withCategory("Restaurant")
                .withAddress("Main Street 1")
                .withPhone("111-222")
                .withEmail("contact@mariopizza.com")
                .withWebsite("www.mariopizza.com")
                .build();
        pizza.setCoordinates(mapService.geocode(pizza.getAddress()));

        BusinessProfile tech = new BusinessProfileBuilder()
                .withId(2L)
                .withName("Tech Repair")
                .withCategory("Electronics")
                .withAddress("Industrial Park 5")
                .withPhone("333-444")
                .withEmail("support@techrepair.com")
                .withWebsite("www.techrepair.com")
                .build();
        tech.setCoordinates(mapService.geocode(tech.getAddress()));

        List<BusinessProfile> repo = new ArrayList<>();
        repo.add(pizza);
        repo.add(tech);

        System.out.println("Built: " + pizza);
        System.out.println("Built: " + tech);
        System.out.println();

        // ----- STRATEGY: different ways to search -----
        SearchService searchService = new SearchService(repo, mapService);

        System.out.println("=== STRATEGY: search by name ===");
        searchService.setStrategy(new NameSearch());
        List<BusinessProfile> byName = searchService.execute("Mario");
        printResults(byName);

        System.out.println("=== STRATEGY: search by category ===");
        searchService.setStrategy(new CategorySearch());
        List<BusinessProfile> byCategory = searchService.execute("Restaurant");
        printResults(byCategory);

        System.out.println("=== STRATEGY: search by location ===");
        searchService.setStrategy(new LocationSearch(5.0)); // radius 5 km
        List<BusinessProfile> nearby = searchService.execute("Central Square");
        printResults(nearby);

        // ----- OBSERVER: user leaves a review -----
        System.out.println("=== OBSERVER: user posts a review ===");
        Customer john = new Customer("John Doe");
        reviewService.postReview(john, pizza, 5, "Great pizza and fast delivery!");
    }

    private static void printResults(List<BusinessProfile> results) {
        if (results.isEmpty()) {
            System.out.println("  No results.");
        } else {
            for (BusinessProfile b : results) {
                System.out.println("  - " + b);
            }
        }
        System.out.println();
    }
}
