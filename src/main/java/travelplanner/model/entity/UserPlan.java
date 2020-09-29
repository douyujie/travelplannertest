package travelplanner.model.entity;

import java.io.Serializable;

import javax.persistence.*;

@Entity
@Table(name = "userplan",
        indexes = {@Index(name = "idx_id", columnList = "id",unique = true)})
@lombok.Data
public class UserPlan implements Serializable {
    private static final long serialVersionUID = 2652327633296064143L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.EAGER)
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    private Plan plan;
}
