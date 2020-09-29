package travelplanner.model.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;

@Entity
@Table(name = "city",
        indexes = {@Index(name = "idx_name", columnList = "name",unique = true)})
@lombok.Data
public class City implements Serializable {
    private static final long serialVersionUID = 8734140534986494039L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;


}
