package cclab.mino.group.domain;

//import cclab.mino.common.PointType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
// import org.springframework.data.geo.Polygon;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Table(name = "activity_area")
public class ActivityArea {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "group_i" +
            "d", referencedColumnName = "id") // 외래 키 이름 수정
    private Group group;

    @ElementCollection
    @CollectionTable(name = "activity_points", joinColumns = @JoinColumn(name = "activity_id"))
    @Column(name="activity_area")
    @Builder.Default
    private List<String> activityArea = new ArrayList<>();
}