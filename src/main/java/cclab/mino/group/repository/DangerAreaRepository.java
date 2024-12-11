package cclab.mino.group.repository;

import cclab.mino.group.domain.DangerArea;
import cclab.mino.group.domain.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DangerAreaRepository extends JpaRepository<DangerArea, Long> {
    List<DangerArea> findDangerAreaByGroup(Group group);
}
