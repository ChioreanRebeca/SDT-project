public interface MapService {
    Coordinates geocode(String address);
    double distanceKm(Coordinates a, Coordinates b);
}

