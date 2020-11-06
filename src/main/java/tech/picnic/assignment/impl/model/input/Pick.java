package tech.picnic.assignment.impl.model.input;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.time.Instant;

public final class Pick implements Serializable {

    private static final long serialVersionUID = -5828057819655702251L;

    private final String id;
    private final Instant timestamp;
    private final Picker picker;
    private final Article article;
    private final Integer quantity;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public Pick(@JsonProperty("id") String id,
                @JsonProperty("timestamp") Instant timestamp,
                @JsonProperty("picker") Picker picker,
                @JsonProperty("article") Article article,
                @JsonProperty("quantity") Integer quantity) {
        this.id = id;
        this.timestamp = timestamp;
        this.picker = picker;
        this.article = article;
        this.quantity = quantity;
    }

    public String getId() {
        return id;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public Picker getPicker() {
        return picker;
    }

    public Article getArticle() {
        return article;
    }

    public Integer getQuantity() {
        return quantity;
    }
}
