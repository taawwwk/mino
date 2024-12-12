package cclab.mino.user.repository;

import cclab.mino.user.domain.Statuses;
import cclab.mino.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface StatusesRepostiory extends JpaRepository<Statuses, Long> {
    // uid를 이용해 Status를 업데이트한다.
    // @Modifying, @Query를 사용한 업데이트 쿼리 작성 시, 업데이트한 행(row)의 수를 반환하기 때문에 int형으로 선언
    @Modifying
    @Transactional
    @Query("UPDATE Statuses s SET s.healthStatus = :healthStatus, s.dangerStatus = :dangerStatus, s.deviationStatus = :deviationStatus, s.gatherStatus = :gatherStatus WHERE s.user.id = :uid")
    int updateStatusesByUser(@Param("uid") long uid,
                             @Param("healthStatus") int healthStatus,
                             @Param("dangerStatus") int dangerStatus,
                             @Param("deviationStatus") int deviationStatus,
                             @Param("gatherStatus") int gatherStatus);

    // 유저 건강 상태(Helath Status) 업데이트 쿼리
    @Modifying
    @Transactional
    @Query("UPDATE Statuses s SET s.healthStatus = :healthStatus WHERE s.user.id = :uid") // 문법 이게 맞나?
    int updateHealthStatusByUser(@Param("uid") long uid, @Param("healthStatus") int healthStatus);

    // 유저의 건강 상태 가져오기
    Statuses findByUser(User user);

    // 건강 상태 삭제 쿼리. 유저가 그룹 탈퇴 시 함꼐 호출되어야 함
    @Transactional
    int deleteAllByUser(User user);
}
