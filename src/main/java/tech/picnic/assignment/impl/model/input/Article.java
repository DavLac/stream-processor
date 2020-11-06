package tech.picnic.assignment.impl.model.input;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public final class Article implements Serializable {

    private static final long serialVersionUID = 1410967606004227772L;

    private final String id;
    private final String name;
    private final TemperatureZoneEnum temperature_zone;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public Article(@JsonProperty("id") String id,
                   @JsonProperty("name") String name,
                   @JsonProperty("temperature_zone") TemperatureZoneEnum temperature_zone) {
        this.id = id;
        this.name = name;
        this.temperature_zone = temperature_zone;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public TemperatureZoneEnum getTemperature_zone() {
        return temperature_zone;
    }
}
