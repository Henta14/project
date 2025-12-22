package observer;

public class RepoEvent {
    private final RepoEventType type;
    private final Object payload;

    public RepoEvent(RepoEventType type, Object payload) {
        this.type = type;
        this.payload = payload;
    }

    public RepoEventType getType() { return type; }
    public Object getPayload() { return payload; }
}
