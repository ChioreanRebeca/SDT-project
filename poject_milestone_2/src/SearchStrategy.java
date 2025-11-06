import java.util.List;

public interface SearchStrategy {
    List<BusinessProfile> search(String query,
                                 List<BusinessProfile> repo,
                                 MapService mapService);
}

