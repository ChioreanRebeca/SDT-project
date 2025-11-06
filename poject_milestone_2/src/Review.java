public class Review {
    private final Customer author;
    private final BusinessProfile business;
    private final int rating;
    private final String text;

    public Review(Customer author, BusinessProfile business, int rating, String text) {
        this.author = author;
        this.business = business;
        this.rating = rating;
        this.text = text;
    }

    public Customer getAuthor() { return author; }
    public BusinessProfile getBusiness() { return business; }
    public int getRating() { return rating; }
    public String getText() { return text; }
}

