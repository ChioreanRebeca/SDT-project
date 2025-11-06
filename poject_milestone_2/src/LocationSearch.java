import java.util.ArrayList;
import java.util.List;

public class LocationSearch implements SearchStrategy {
    private final double radiusKm;

    public LocationSearch(double radiusKm) {
        this.radiusKm = radiusKm;
    }

    @Override
    public List<BusinessProfile> search(String query, List<BusinessProfile> repo, MapService mapService) {
        System.out.println("[Strategy: LocationSearch] Searching near '" + query + "', radius = "
                + radiusKm + " km");

        Coordinates userCoords = mapService.geocode(query);

        List<BusinessProfile> result = new ArrayList<>();
        for (BusinessProfile b : repo) {
            if (b.getCoordinates() == null) continue;
            double dist = mapService.distanceKm(userCoords, b.getCoordinates());
            if (dist <= radiusKm) {
                result.add(b);
            }
        }
        return result;
    }
}

