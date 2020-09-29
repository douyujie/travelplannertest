package travelplanner.model.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;

@Entity
@Table(name = "user",
        indexes = {@Index(name = "idx_username", columnList = "username",unique = true)})
@lombok.Data
public class User implements Serializable {
    private static final long serialVersionUID = 6571020025726257848L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String username;

    private String password;

    private boolean enabled;
}
