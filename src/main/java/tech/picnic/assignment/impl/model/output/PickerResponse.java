package tech.picnic.assignment.impl.model.output;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;

public class PickerResponse implements Serializable {

    private static final long serialVersionUID = -4141526024965646529L;

    private String picker_name;
    private Instant active_since;
    private List<PickResponse> picks;

    public PickerResponse(String picker_name, Instant active_since, List<PickResponse> picks) {
        this.picker_name = picker_name;
        this.active_since = active_since;
        this.picks = picks;
    }

    public String getPicker_name() {
        return picker_name;
    }

    public void setPicker_name(String picker_name) {
        this.picker_name = picker_name;
    }

    public Instant getActive_since() {
        return active_since;
    }

    public void setActive_since(Instant active_since) {
        this.active_since = active_since;
    }

    public List<PickResponse> getPicks() {
        return picks;
    }

    public void setPicks(List<PickResponse> picks) {
        this.picks = picks;
    }
}
