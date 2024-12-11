package cclab.mino.group.repository;

import cclab.mino.group.domain.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository

public interface GroupRepository extends JpaRepository<Group, Long> {
    // Group findGroupByGid(String gid);
    Group findByGid(String gid);
    boolean existsByGid(String gid);
    // boolean updateGroupByEnableState(String gid);
    @Modifying
    @Transactional
    @Query("update Group g set g.enableState = true where g.gid =:gid")
    int updateEnableStateTrue(String gid);

    @Modifying
    @Transactional
    @Query("update Group g set g.enableState = false where g.gid =:gid")
    int updateEnableStateFalse(String gid);
}
