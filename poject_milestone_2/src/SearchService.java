import java.util.List;

public class SearchService {
    private SearchStrategy strategy;
    private final List<BusinessProfile> repo;
    private final MapService mapService;

    public SearchService(List<BusinessProfile> repo, MapService mapService) {
        this.repo = repo;
        this.mapService = mapService;
    }

    public void setStrategy(SearchStrategy strategy) {
        this.strategy = strategy;
    }

    public List<BusinessProfile> execute(String query) {
        if (strategy == null) {
            throw new IllegalStateException("Search strategy not set");
        }
        return strategy.search(query, repo, mapService);
    }
}

