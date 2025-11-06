import java.util.List;

public class CategorySearch implements SearchStrategy {
    @Override
    public List<BusinessProfile> search(String query, List<BusinessProfile> repo, MapService mapService) {
        System.out.println("[Strategy: CategorySearch] Searching for category '" + query + "'");
        return repo.stream()
                .filter(b -> b.getCategory().equalsIgnoreCase(query))
                .toList();
    }
}

