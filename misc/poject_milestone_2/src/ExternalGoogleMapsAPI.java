public class ExternalGoogleMapsAPI {

    public Coordinates getCoordinates(String address) {
        // FAKE implementation – just for demo, returns fixed values
        if (address.contains("Main Street 1")) {
            return new Coordinates(0, 0);
        } else if (address.contains("Industrial Park 5")) {
            return new Coordinates(8, 0);
        } else if (address.contains("Central Square")) {
            return new Coordinates(1, 1);
        } else {
            return new Coordinates(5, 5);
        }
    }

    public double calculateDistanceInKm(Coordinates a, Coordinates b) {
        double dx = a.getLat() - b.getLat();
        double dy = a.getLon() - b.getLon();
        return Math.sqrt(dx * dx + dy * dy); // Euclidean; units don't matter in the demo
    }
}
