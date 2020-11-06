package tech.picnic.assignment.impl.model.input;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

public final class Picker implements Serializable, Comparable<Picker> {

    private static final long serialVersionUID = 3012771280175373015L;

    private final String id;
    private final String name;
    private final Instant active_since;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public Picker(@JsonProperty("id") String id,
                  @JsonProperty("name") String name,
                  @JsonProperty("active_since") Instant active_since) {
        this.id = id;
        this.name = name;
        this.active_since = active_since;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Instant getActive_since() {
        return active_since;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Picker)) return false;
        Picker picker = (Picker) o;
        return getId().equals(picker.getId()) &&
                getName().equals(picker.getName()) &&
                getActive_since().equals(picker.getActive_since());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getActive_since());
    }

    @Override
    public int compareTo(Picker otherPicker) {
        if (this.equals(otherPicker)) {
            return 0;
        }

        int dateComparison = getActive_since().compareTo(otherPicker.getActive_since());

        // If 2 pickers have a different Id and the same active_since date, they shouldn't be grouped
        if (dateComparison == 0 && CharSequence.compare(getId(), otherPicker.getId()) != 0) {
            return 1;
        }

        return dateComparison;
    }

}
