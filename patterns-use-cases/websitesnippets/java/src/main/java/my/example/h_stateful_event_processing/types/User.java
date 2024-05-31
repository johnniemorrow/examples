package my.example.h_stateful_event_processing.types;

import java.util.List;

public class User {

    private String userId;
    private List<Feature> features;

    public User() {
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void addFeature(Feature feature) {
        this.features.add(feature);
    }
}
