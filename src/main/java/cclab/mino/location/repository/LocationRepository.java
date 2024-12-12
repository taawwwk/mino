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
    // 유저의 위치 정보 존재 여부 검사 쿼리
    Boolean existsByUser(User user);

    // 유저 위치 정보 업데이트 쿼리
    @Modifying
    @Transactional
    @Query("update Locations l set l.latitude = :latitude, l.longitude = :longitude where l.user = :user")
    int updateLocation(User user, double latitude, double longitude);

    // 유저 객체로 위치 정보 가져오기
    Locations findByUser(User user);


}
