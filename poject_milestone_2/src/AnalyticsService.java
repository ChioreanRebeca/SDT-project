public class AnalyticsService implements EventListener {
    @Override
    public void onEvent(EventType type, Object data) {
        if (type == EventType.REVIEW_POSTED) {
            Review r = (Review) data;
            System.out.println("[AnalyticsService] Track rating " + r.getRating()
                    + " for " + r.getBusiness().getName());
        }
    }
}

