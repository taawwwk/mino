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
    // ID for PK
    private Long id;

    @OneToOne
    @JoinColumn(name = "senior_id", nullable = false)
    // 노인 객체. users table의 id와 외래키
    private User senior;

    @OneToOne
    @JoinColumn(name = "manager_id", nullable = false)
    // 복지사 객체. users table의 id와 외래키
    private User manager;

    @OneToOne
    @JoinColumn(name = "gid", nullable = false)
    // 그룹 객체. groups table의 gid와 외래키
    private Group group;

    @Column(name = "status")
    // 그룹 활성화 상태. True: 활성, False: 비활성
    private Boolean status;
}
