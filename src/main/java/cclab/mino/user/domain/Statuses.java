package cclab.mino.user.domain;

import cclab.mino.group.domain.Group;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * <b>Statuses: 노인의 상태.</b>
 * @healthStatus: 노인의 견강 상태. 핸드폰의 가속도 센서를 이용한 움직임 감지 (범위: 0~1)
 * @dangerStatus: 위험 지역에 접근 or 진입에 대한 정보 (범위: 0~2)
 * @deviationStatus: 할동 범위에서 이탈 or 이탈 위험에 대한 정보 (범위: 0~2)
 * @gatherStatus: 집결 중 발생하는 정상, 경로 이탈, 위험에 대한 이벤트 (범위: 0~2)
 */
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="statuses")
public class Statuses {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @OneToOne
    @JoinColumn(name = "uid", nullable = false)
    private User user;

    @Column(name="health_status")
    private int healthStatus;

    @Column(name="danger_status")
    private int dangerStatus;

    @Column(name="deviation_status")
    private int deviationStatus;

    @Column(name="gather_status")
    private int gatherStatus;
}
