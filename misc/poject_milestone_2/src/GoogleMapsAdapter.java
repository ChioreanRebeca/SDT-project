public class GoogleMapsAdapter implements MapService {
    private final ExternalGoogleMapsAPI api;

    public GoogleMapsAdapter(ExternalGoogleMapsAPI api) {
        this.api = api;
    }

    @Override
    public Coordinates geocode(String address) {
        System.out.println("[Adapter] Geocoding '" + address + "' via ExternalGoogleMapsAPI");
        return api.getCoordinates(address);
    }

    @Override
    public double distanceKm(Coordinates a, Coordinates b) {
        double dist = api.calculateDistanceInKm(a, b);
        System.out.println("[Adapter] Distance = " + String.format("%.2f", dist) + " km");
        return dist;
    }
}

