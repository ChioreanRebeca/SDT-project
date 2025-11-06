public class ReviewService {
    private final EventBus eventBus;

    public ReviewService(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    public void postReview(Customer customer, BusinessProfile business, int rating, String text) {
        System.out.println("[ReviewService] Posting review...");
        Review review = new Review(customer, business, rating, text);
        business.addReview(review);
        eventBus.publish(EventType.REVIEW_POSTED, review);
        System.out.println("[ReviewService] New rating for " + business.getName()
                + " = " + String.format("%.2f", business.getRating()));
    }
}

