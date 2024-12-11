package cclab.mino.user.domain;

import cclab.mino.group.domain.Group;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "uid")
    private Integer uid;

    @Column(name = "rid")
    private Integer rid;

    @Column(name = "user_name")
    private String userName;

    @Column(name="tel")
    private String tel;

    @ManyToOne
    @JoinColumn(name = "gid", nullable = false)
    private Group group;

//    @Column(name = "tel")
//    private String tel;

    /*@OneToOne(mappedBy = "user")
    private Location location;*/

    /*@OneToMany(mappedBy = "user")
    private Set<Manager> manager;*/

    /*@OneToMany(mappedBy = "user")
    private Set<Senior> senior;*/
}
