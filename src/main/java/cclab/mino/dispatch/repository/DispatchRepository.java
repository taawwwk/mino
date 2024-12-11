package cclab.mino.dispatch.repository;

import cclab.mino.dispatch.domain.Dispatch;
import cclab.mino.group.domain.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DispatchRepository extends JpaRepository<Dispatch,Long> {
    Boolean existsDispatchByGroup(Group group);
    Dispatch findDispatchByGroup(Group group);
}
