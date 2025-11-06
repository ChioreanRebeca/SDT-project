import java.util.ArrayList;
import java.util.List;

public class BusinessProfile {
    private final long id;
    private final String name;
    private final String category;
    private final String address;
    private final String phoneNumber;
    private final String email;
    private final String website;

    private Coordinates coordinates;             // used by LocationSearch
    private final List<Review> reviews = new ArrayList<>();
    private double rating = 0.0;

    public BusinessProfile(long id,
                           String name,
                           String category,
                           String address,
                           String phoneNumber,
                           String email,
                           String website) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.website = website;
    }

    // ---- business logic ----
    public void addReview(Review review) {
        reviews.add(review);
        updateRating();
    }

    private void updateRating() {
        if (reviews.isEmpty()) {
            rating = 0;
            return;
        }
        int sum = 0;
        for (Review r : reviews) {
            sum += r.getRating();
        }
        rating = sum / (double) reviews.size();
    }

    // ---- getters & setters ----
    public long getId() { return id; }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public String getAddress() { return address; }
    public double getRating() { return rating; }

    public Coordinates getCoordinates() { return coordinates; }
    public void setCoordinates(Coordinates coordinates) { this.coordinates = coordinates; }

    @Override
    public String toString() {
        return name + " (" + category + ", " + address + ", rating=" + String.format("%.2f", rating) + ")";
    }
}

