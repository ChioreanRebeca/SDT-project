public class BusinessProfileBuilder {
    private long id;
    private String name;
    private String category;
    private String address;
    private String phoneNumber;
    private String email;
    private String website;

    public BusinessProfileBuilder withId(long id) {
        this.id = id;
        return this;
    }

    public BusinessProfileBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public BusinessProfileBuilder withCategory(String category) {
        this.category = category;
        return this;
    }

    public BusinessProfileBuilder withAddress(String address) {
        this.address = address;
        return this;
    }

    public BusinessProfileBuilder withPhone(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public BusinessProfileBuilder withEmail(String email) {
        this.email = email;
        return this;
    }

    public BusinessProfileBuilder withWebsite(String website) {
        this.website = website;
        return this;
    }

    public BusinessProfile build() {
        return new BusinessProfile(id, name, category, address, phoneNumber, email, website);
    }
}
