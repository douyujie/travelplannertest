package travelplanner.model.entity;

import java.io.Serializable;

import javax.persistence.*;

@Entity
@Table(name = "routeattraction")
@lombok.Data
public class RouteAttraction implements Serializable {
    private static final long serialVersionUID = 7551999649936522523L;

    @EmbeddedId
    private RouteAttractionId id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "route_id", nullable = false, insertable = false, updatable = false)
    private Route route;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "attraction_id", nullable = false, insertable = false, updatable = false)
    private Attraction attraction;

    @Column(name = "order_value")
    private int order;

    public void prePersiste() {
        if (getId() == null) {
            RouteAttractionId id = new RouteAttractionId();
            id.setAttraction(attraction);
            id.setRoute(route);
            setId(id);
        }
    }
}
