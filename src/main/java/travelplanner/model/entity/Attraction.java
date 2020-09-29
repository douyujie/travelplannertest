package travelplanner.model.entity;

import travelplanner.model.UserPlanData;

import java.io.Serializable;

import javax.persistence.*;

@Entity
@Table(name = "attraction",
indexes = {@Index(name = "idx_id", columnList = "id",unique = true)})
@lombok.Data
public class Attraction implements Serializable {
    private static final long serialVersionUID = 2718281828459045235L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    private float latitude;

    private float longitude;

    private String type;

    private float rating;

    private String hashcode;

    @Override
    public int hashCode() {
        return (int)(latitude * 101 + longitude);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Attraction) {
            Attraction attraction = (Attraction) o;
            return attraction.latitude == this.latitude &&
                    attraction.longitude == this.longitude;
        }
        return false;
    }
}
