package travelplanner.model.entity;

import java.io.Serializable;

import javax.persistence.*;

@Entity
@Table(name = "route",
        indexes = {@Index(name = "idx_id", columnList = "id",unique = true)})
@lombok.Data
public class Route implements Serializable {
    private static final long serialVersionUID = 1028098616457762743L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    private Plan plan;

    private int day;

    private String hashcode;
}
