import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventBus {
    private final Map<EventType, List<EventListener>> listeners = new HashMap<>();

    public void subscribe(EventType type, EventListener listener) {
        listeners.computeIfAbsent(type, t -> new ArrayList<>()).add(listener);
    }

    public void publish(EventType type, Object data) {
        System.out.println("[EventBus] Publishing event " + type);
        List<EventListener> ls = listeners.get(type);
        if (ls == null) return;
        for (EventListener listener : ls) {
            listener.onEvent(type, data);
        }
    }
}

