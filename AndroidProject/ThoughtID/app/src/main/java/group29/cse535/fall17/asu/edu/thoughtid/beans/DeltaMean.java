package group29.cse535.fall17.asu.edu.thoughtid.beans;

/**
 * Created by sgollana on 11/26/2017.
 */

public class DeltaMean {
    String id;
    String value;

    public DeltaMean(String id, String value) {
        this.id = id;
        this.value = value;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
