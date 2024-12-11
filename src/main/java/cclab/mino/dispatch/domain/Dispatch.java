package cclab.mino.dispatch.domain;

import cclab.mino.group.domain.Group;
import cclab.mino.user.domain.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "dispatch")
public class Dispatch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "senior_id", nullable = false)
    private User senior;

    @OneToOne
    @JoinColumn(name = "manager_id", nullable = false)
    private User manager;

    @OneToOne
    @JoinColumn(name = "gid", nullable = false)
    private Group group;

    @Column(name = "status")
    private Boolean status;
}
