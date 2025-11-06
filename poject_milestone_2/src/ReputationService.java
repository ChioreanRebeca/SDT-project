public class ReputationService implements EventListener {
    @Override
    public void onEvent(EventType type, Object data) {
        if (type == EventType.REVIEW_POSTED) {
            Review r = (Review) data;
            System.out.println("[ReputationService] Recalculate reputation for "
                    + r.getBusiness().getName());
        }
    }
}
