package cclab.mino.location.repository;

import cclab.mino.location.domain.Locations;
import cclab.mino.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface LocationRepository extends JpaRepository<Locations, Long> {
    Boolean existsByUser(User user);

    @Modifying
    @Transactional
    @Query("update Locations l set l.latitude = :latitude, l.longitude = :longitude where l.user = :user")
    int updateLocation(User user, double latitude, double longitude);

    Locations findByUser(User user);


}
