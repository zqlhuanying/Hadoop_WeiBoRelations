package Beans.ForceBeans;

/**
 * Created by root on 15-5-17.
 */
public class Edge {

    private String distance;
    //private Nodes source;
    //private Nodes target;
    private Integer source;
    private Integer target;

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public Integer getSource() {
        return source;
    }

    public void setSource(Integer source) {
        this.source = source;
    }

    public Integer getTarget() {
        return target;
    }

    public void setTarget(Integer target) {
        this.target = target;
    }
}
