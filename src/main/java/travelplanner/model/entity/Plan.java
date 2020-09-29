package travelplanner.model.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;

@Entity
@Table(name = "plan",
        indexes = {@Index(name = "idx_hashcode", columnList = "hashcode",unique = true)})
@lombok.Data
public class Plan implements Serializable {
    private static final long serialVersionUID = 8436097833452420298L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    private String hashcode;

    @ManyToOne
    private City city;

    @OneToMany(mappedBy = "plan", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Route> routes;

    @Override
    public boolean equals(Object o){
        if (o instanceof Plan){
            Plan plan = (Plan) o;
            return plan.getId() == this.id;
        }
        return false;
    }

    @Override
    public int hashCode(){
        return this.id;
    }
}
