package cclab.mino.group.domain;

import cclab.mino.user.domain.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Set;
import java.util.List;

/**
 * <b>Group</b>
 * @gid: 그룹의 id. 노인과 복지사가 이 gid를 이용해 입장하고, 그룹 관련 데이터 조작, 조회할 때 gid를 사용
 * @name: 그룹 생성한 복지사의 이름
 * @org: 그룹 생성한 복지사의 소속 기관 이름
 * @tel: 그룹 생성한 복지사의 전화번호
 * @capacity: 자신의 그룹에 참여가능한 제한 인원 수
 * @createAt: 그룹이 생성된 시간. 추후에 그룹 자동 삭제, 연장 등에 필요한 값
 * @enableState: 위치 모니터링 단계 활성화 여부를 나타낸다. 그룹 인원수가 가득 차면, 활성화
 * @users: 그룹에 소속된 유저 객체. user의 id(uid)와 외래키 관계를 맺음
 * @activities: 복지사가 설정한 노인의 활동 범위에 대한 배열
 * @dangerAreas: 복지사가 설정한 노인의 접근 위험 지역 배열
 */
@NoArgsConstructor
@Builder
@Getter
@AllArgsConstructor
@Entity
@Table(name = "groups")
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "gid")
    private String gid;

    @Column(name = "name")
    private String name;

    @Column(name = "org")
    private String org;

    @Column(name = "tel")
    private String tel;

    @Column(name = "capacity")
    private Integer capacity;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "enable_state")
    private Boolean enableState;

    @OneToMany(mappedBy = "group") // 그룹과 유저 매핑
    private Set<User> users;

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ActivityArea> activities = new ArrayList<>();

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DangerArea> dangerAreas = new ArrayList<>();
}