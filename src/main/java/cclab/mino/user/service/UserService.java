package cclab.mino.user.service;

import cclab.mino.common.GroupManagement;
import cclab.mino.group.domain.DangerArea;
import cclab.mino.group.domain.Group;
import cclab.mino.group.repository.ActivityRepository;
import cclab.mino.group.repository.DangerAreaRepository;
import cclab.mino.user.domain.Statuses;
import cclab.mino.user.dto.ManagersDTO;
import cclab.mino.user.dto.SeniorsDTO;
import cclab.mino.user.dto.UserDTO;
import cclab.mino.group.repository.GroupRepository;
import cclab.mino.user.domain.User;
import cclab.mino.user.repository.StatusesRepostiory;
import cclab.mino.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final StatusesRepostiory statusesRepostiory;
    private final ActivityRepository activityRepository;
    private final DangerAreaRepository dangerAreaRepository;

    public UserService(UserRepository userRepository, GroupRepository groupRepository,
                       StatusesRepostiory statusesRepostiory, ActivityRepository activityRepository,
                       DangerAreaRepository dangerAreaRepository) {
        this.userRepository = userRepository;
        this.groupRepository = groupRepository;
        this.statusesRepostiory = statusesRepostiory;
        this.activityRepository = activityRepository;
        this.dangerAreaRepository = dangerAreaRepository;
    }

    /**
     * 노인을 위한 그룹 참여 및 uid 발급
     * @param group
     * @return
     */
    @Transactional
    public Long issueUidforSenior(Group group){
        /*
         *  유저 객체에게 파라미터 group의 gid를 부여해줌
         *  rid(Role ID): 0은 노인을 뜻한다.
         *  모든 정보를 입력하고 저장
         */
        User user = User.builder()
                .group(group)
                .rid(0)
                .userName(null)
                .build();
        System.out.println("id 출력: " + group.getId());
        System.out.println("gid 출력: " + group.getGid());
        userRepository.save(user); // 유저 테이블에 노인에 대한 정보를 저장한다.

        /*
         * 노인의 상태(Statuses) 초기 값 설정하고 저장.
         */
        Statuses statuses = Statuses.builder()
                .user(user)
                .healthStatus(0)
                .dangerStatus(0)
                .deviationStatus(0)
                .gatherStatus(0)
                .build();

        // statuses에 저장
        statusesRepostiory.save(statuses);

        return user.getId();
    }

    /**
     * 복지사를 위한 그룹 참여 및 uid 발급
     * @param group
     * @param name
     * @param tel
     * @return uid
     */
    @Transactional
    public Long issueUidForManager(Group group, String name, String tel){
        /*
         *  유저 객체에게 파라미터 group의 gid를 부여해줌
         *  rid(Role ID): 1은 복지사를 뜻함
         *  파라미터로 받은 name, tel으로 복지사에 대한 정보를 저장.
         */
        User user = User.builder()
                .group(group)
                .rid(1) // 복지사 rid는 1임
                .userName(name)
                .tel(tel)
                .build();
        System.out.println("id 출력: " + group.getId());
        System.out.println("gid 출력: " + group.getGid());

        userRepository.save(user); // User 테이블에 저장

        return user.getId();
    }

    /**
     * 그룹에 소속된 유저 정보(노인, 복지사) 가져오기
     * @param gid
     * @return UserDTO (have Seniors objects, Managers objects)
     */
    @Transactional
    public UserDTO getUsers(long gid){
        // 파라미터의 gid와 일치하는 모든 유저 가져오기
        List<User> users= userRepository.findAllByGroupId(gid);

        // 노인 배열
        ArrayList<SeniorsDTO> seniorArr = new ArrayList<>();
        // 복지사 배열
        ArrayList<ManagersDTO> managerArr = new ArrayList<>();

        /*
        * users 배열의 user 객체마다 가진 rid에 따라 노인, 복지사 구분
        * 구분한 노인, 복지사를 반환 값에 넣기
        */
        for (int i = 0; i<users.size(); i++){
            if (users.get(i).getRid() == 0) {
                seniorArr.add(
                        SeniorsDTO.builder()
                                .uid(users.get(i).getId())
                                .seniorName(users.get(i).getUserName())
                                .build()
                );
            }

            else if (users.get(i).getRid() == 1 || users.get(i).getRid() == 2){
                managerArr.add(
                        ManagersDTO.builder()
                                .uid(users.get(i).getId())
                                .managerName(users.get(i).getUserName())
                                .managerTel(users.get(i).getTel())
                                .build()
                );
            }
        }

        System.out.println("managersDTO size in Service: " + managerArr.size());
        System.out.println("seniorsDTO size in Service: " + seniorArr.size());
        return UserDTO.builder()
                .seniors(seniorArr)
                .managers(managerArr)
                .build();
    }

    /**
     * 노인 이름 저장 for 그룹생성자
     * @param seniors (유저 id, 노인 이름)
     */
    @Transactional
    public void saveSeniorsName(ArrayList<SeniorsDTO> seniors){
        /*
        * dto의 seniorArrayListDTO에서 하나씩 꺼내오기
        * dto에서 아이디, 이름 추출
        * id를 기준으로, 이름 열 업데이트
        * 저장하기
         */
        for (int i=0; i<seniors.size(); i++){
            long uid = seniors.get(i).getUid();
            String sName = seniors.get(i).getSeniorName();
            System.out.println("노인 uid: " + uid + ", 노인 이름: " + sName);
            // User senior = userRepository.findUserById(uid);
            // senior.setUserName(sName);
            // =============== 업데이트가 안된다. =============== -> 왜 주석처리했더라?
             userRepository.updateUserName(uid,sName); // uid에 일치한 행을 찾아 노인 이름 업데이트
            // userRepository.updateUserNameByUid(uid, sName);
            // userRepository.flush();
        }
    }

    /**
     * 그룹 입장 가능 여부 확인.
     * @param gid
     * @return True: 참여가능, False: 참여불가. 그룹이 꽉 찼음
     */
    public Boolean checkGroupCapacity (String gid){
        // 그룹 관리 객체 생성
        GroupManagement groupManagement = new GroupManagement(groupRepository);
        // gid와 일치하는 그룹 찾기
        Group group = groupManagement.findGroup(gid);
        if (group == null){
            return false;
        }
        else {
            // 그룹 생성 시 설정한 수용 인원 조회
            int groupCapacity = groupManagement.getGroupCapacity(group);
            System.out.println("수용 인원: " + groupCapacity);

            // gid와 일치하는 유저 수 조회
            int currentUsers = userRepository.countUserByGroupId(group.getId());
            System.out.println("현재 인원 수: " + currentUsers);

            // 유저 수, 수용인원 체크.
            // 참이면 활성화 가능, 거짓이면 불가능
            Boolean checkCapacity = groupManagement.compareCount(groupCapacity, currentUsers);

            System.out.println("수용 인원: " + groupCapacity + ", 현재 인원 수: " + currentUsers);
            System.out.println("그룹 활성화 가능? " + checkCapacity);
            return checkCapacity;
        }
    }

    /**
     * 유저 삭제
     * @param uid
     * @return 삭제 여부
     */
    public Boolean deleteUser(long uid){
        if (userRepository.findUserById(uid) == null){
            return false;
        }
        User user = userRepository.findUserById(uid);
        int rid = user.getRid();

        // 노인(rid=0)이면 상태(Statuses)도 함께 삭제
        if (rid==0){
            statusesRepostiory.deleteAllByUser(user);
            userRepository.deleteById(uid);
        }
        else {
            userRepository.deleteById(uid);
        }
        return true;
    }

    /**
     * 그룹의 Activity Area를 반환
     * @param group
     * @return activiyArea: String List
     */
    public List<String> findActivityArea(Group group){
        List<String> activityArea = activityRepository.findActivityAreaByGroup(group).getActivityArea();
        return activityArea;
    }

    /**
     * 그룹의 Danger Area를 반환
     * @param group
     * @return activiyArea: String List
     */
    public List<List<String>> findDangerAreaArea(Group group){
        List<DangerArea> dangerAreas = dangerAreaRepository.findDangerAreaByGroup(group);
        List<List<String>> dangerAreaList = new ArrayList<>();
        for (int i=0; i<dangerAreas.size(); i++){
            DangerArea dangerArea = dangerAreas.get(i);
            dangerAreaList.add(dangerArea.getDangerArea());
        }
        return dangerAreaList;
    }

}
