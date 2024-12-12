package cclab.mino.dispatch.service;

import cclab.mino.common.GroupManagement;
import cclab.mino.common.UserManagement;
import cclab.mino.dispatch.domain.Dispatch;
import cclab.mino.dispatch.repository.DispatchRepository;
import cclab.mino.group.domain.Group;
import cclab.mino.group.repository.GroupRepository;
import cclab.mino.user.domain.User;
import cclab.mino.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DispatchService {
    private final DispatchRepository dispatchRepository;
    private final UserRepository userRepository;
    private final GroupRepository groupRepository;

    public DispatchService(DispatchRepository dispatchRepository, UserRepository userRepository, GroupRepository groupRepository) {
        this.dispatchRepository = dispatchRepository;
        this.userRepository = userRepository;
        this.groupRepository = groupRepository;
    }


    /**
     * 출동 신호 설정이 가능한 범위(같은 그룹)인지 확인
     * @param seniorUid
     * @param managerUid
     * @return boolean
     */
    public boolean enableDispatch(long seniorUid, long managerUid){
        UserManagement userManagement = new UserManagement(userRepository);
        User senior = userManagement.findUser(seniorUid);
        User manager = userManagement.findUser(managerUid);

        if (senior==null || manager==null){
            System.out.println("존재하지 않는 유저입니다.");
            return false;
        }

        if (senior.getGroup().getId() != manager.getGroup().getId()){
            System.out.println("서로 다른 그룹입니다.");
            System.out.println("노인의 그룹 id: " + senior.getGroup().getId());
            System.out.println("복지사의 그룹 id: " + manager.getGroup().getId());
            return false;
        }
        return true;
    }

    /**
     * 출동 신호 저장.
     * @param gid
     * @param seniorUid -> 대상 노인
     * @param managerUid -> 출동할 복지사
     * @return
     */
    @Transactional
    public boolean setDispatchSign(String gid, long seniorUid, long managerUid){
        UserManagement userManagement = new UserManagement(userRepository);
        GroupManagement groupManagement = new GroupManagement(groupRepository);

        User senior = userManagement.findUser(seniorUid);
        User manager = userManagement.findUser(managerUid);
        Group group = groupManagement.findGroup(gid);

        // TODO: 유저 존재 여부 검사는 common's userManagement로 이동하기
        if (senior==null || manager==null){
            System.out.println("존재하지 않는 유저입니다.");
            return false;
        }

        Dispatch dispatch = Dispatch.builder()
                .manager(manager)
                .senior(senior)
                .status(true)
                .group(group)
                .build();

        dispatchRepository.save(dispatch);

        return true;
    }

    /**
     * 출동 신호 존재 여부 검사
     * @param gid
     * @return
     */
    public Dispatch checkDispatchSign(String gid){
        GroupManagement groupManagement = new GroupManagement(groupRepository);
        Group group = groupManagement.findGroup(gid);
        // group이 null로 찍힘.
        System.out.println("그룹 정보: " + group);

        Boolean exist = dispatchRepository.existsDispatchByGroup(group);

        if (exist){
            Dispatch dispatch = dispatchRepository.findDispatchByGroup(group);
            System.out.println("출동 신호 이력이 존재합니다. :" + dispatch.getId());
            return dispatch;

        }
        System.out.println("출동 신호 이력이 존재하지 않습니다.. :" + exist);
        return null;
    }
}
