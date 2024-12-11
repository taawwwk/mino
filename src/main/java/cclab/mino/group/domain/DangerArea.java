package cclab.mino.group.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Point;


import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Entity
@Table(name="danger_area")
public class DangerArea {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="group_id", referencedColumnName = "id") // 외래 키 이름 수정
    private Group group;

    @ElementCollection
    @CollectionTable(name = "danger_points", joinColumns = @JoinColumn(name = "danger_id"))
    @Column(name = "danger_area")
    @Builder.Default
    private List<String> dangerArea = new ArrayList<>();
}