public class NotificationService implements EventListener {
    @Override
    public void onEvent(EventType type, Object data) {
        if (type == EventType.REVIEW_POSTED) {
            Review r = (Review) data;
            System.out.println("[NotificationService] Notify owner: new review for "
                    + r.getBusiness().getName() + " from " + r.getAuthor().getName());
        }
    }
}
