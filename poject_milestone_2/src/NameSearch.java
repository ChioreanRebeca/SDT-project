import java.util.List;

public class NameSearch implements SearchStrategy {
    @Override
    public List<BusinessProfile> search(String query, List<BusinessProfile> repo, MapService mapService) {
        System.out.println("[Strategy: NameSearch] Searching for name containing '" + query + "'");
        return repo.stream()
                .filter(b -> b.getName().toLowerCase().contains(query.toLowerCase()))
                .toList();
    }
}

