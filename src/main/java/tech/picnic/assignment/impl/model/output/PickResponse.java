package tech.picnic.assignment.impl.model.output;

import java.io.Serializable;
import java.time.Instant;

public class PickResponse implements Serializable {

    private static final long serialVersionUID = -5204396805926283348L;

    private String article_name;
    private Instant timestamp;

    public PickResponse(String article_name, Instant timestamp) {
        this.article_name = article_name;
        this.timestamp = timestamp;
    }

    public String getArticle_name() {
        return article_name;
    }

    public void setArticle_name(String article_name) {
        this.article_name = article_name;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }
}
