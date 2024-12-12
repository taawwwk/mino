package cclab.mino.user.repository;

import cclab.mino.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    // gid와 일치하는 유저 리스트르 가져옴
    List<User> findAllByGroupId(long gid);

    // gid를 가진 유저가 몇명인지 체크
    int countUserByGroupId(long gid);

    // 유저 객체 호출 객체
    User findUserById(long uid);

    // 유저 이름 변경 쿼리
    @Modifying
    @Transactional
    @Query("UPDATE User u set u.userName = :sName where u.id = :uid")
    void updateUserName(@Param("uid") long uid, @Param("sName") String sName);

    // 유저 삭제 쿼리
    void deleteById(long uid);
}
