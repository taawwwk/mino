package cclab.mino.group.repository;

import cclab.mino.group.domain.ActivityArea;
import cclab.mino.group.domain.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ActivityRepository extends JpaRepository<ActivityArea,Long> {
    ActivityArea findActivityAreaByGroup(Group group);
}
