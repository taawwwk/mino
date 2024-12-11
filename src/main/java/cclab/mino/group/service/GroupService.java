package cclab.mino.group.service;

import cclab.mino.common.PointConverter;
import cclab.mino.group.domain.ActivityArea;
import cclab.mino.group.domain.DangerArea;
import cclab.mino.group.domain.Group;
import cclab.mino.group.dto.GroupRequestDTO;
import cclab.mino.group.dto.GroupResponseDTO;
import cclab.mino.group.repository.ActivityRepository;
import cclab.mino.group.repository.DangerAreaRepository;
import cclab.mino.group.repository.GroupRepository;
import cclab.mino.user.domain.User;
import cclab.mino.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GroupService {
    private final GroupRepository groupRepository;
    private final ActivityRepository activityRepository;
    private final DangerAreaRepository dangerAreaRepository;
    private final UserRepository userRepository;

    public GroupService(GroupRepository groupRepository, ActivityRepository activityRepository, DangerAreaRepository dangerAreaRepository, UserRepository userRepository) {
        this.groupRepository = groupRepository;
        this.activityRepository = activityRepository;
        this.dangerAreaRepository = dangerAreaRepository;
        this.userRepository = userRepository;
    }

    /**
     * 그룹 생성
     * @param requestDTO
     * @return GroupResponseDTO (gid, uid) -> gid: 생성된 그룹의 id, uid: 그룹을 생성한 유저의 id
     */
    @Transactional
    public GroupResponseDTO generateGroup(GroupRequestDTO requestDTO) {
        // 그룹 엔티티 생성 및 저장
        Group group = Group.builder()
                .gid(randomGid())
                .capacity(requestDTO.getCapacity())
                .org(requestDTO.getOrg())
                .tel(requestDTO.getTel())
                .name(requestDTO.getName())
                .enableState(false) // 추후 입장하는 인원 수에 따라 상태를 변경한다
                .createdAt(LocalDateTime.now())
                .build();

        groupRepository.save(group); // 그룹 정보 저장

        // 그룹 생성한 복지사의 정보를 저장
        User user = User.builder()
                .rid(2)
                .userName(requestDTO.getName())
                .group(group)
                .build();

        userRepository.save(user); // user 테이블에 저장

        // Activity Area 처리
        List<String> activityPoints = requestDTO.getActivityArea().stream()
                .map(PointConverter::parseStringToPointString)
                .collect(Collectors.toList());

        ActivityArea activityArea = ActivityArea.builder()
                .group(group)
                .activityArea(activityPoints)
                .build();

        activityRepository.save(activityArea);

        // Danger Area 처리. DangerArea는 2개 이상 가능
        for (List<String> dangerAreaPoints : requestDTO.getDangerArea()) {
            List<String> dangerPoints = dangerAreaPoints.stream()
                    .map(PointConverter::parseStringToPointString)
                    .collect(Collectors.toList());

            DangerArea dangerArea = DangerArea.builder()
                    .group(group)
                    .dangerArea(dangerPoints)
                    .build();

            dangerAreaRepository.save(dangerArea);
        }

        return new GroupResponseDTO(group.getGid(), user.getId());
    }

    /**
     * 임의의 4자리 gid 생성기
     * @return gid (String, 4자리)
     */
    public String randomGid() {
        // 0부터 9999까지의 난수 생성
        int randInt = (int) (Math.random() * 10000);

        // 4자리 문자열로 변환, 부족한 자리수는 0으로 채움
        String gid = String.format("%04d", randInt);

        // 생성한 gid의 존재 여부를 검사함. 만약 존재한다면 gid 생성함수를 재귀적으로 호출.
        Boolean existStatus = groupRepository.existsByGid(gid);
        if (existStatus) {
            System.out.println("중복 발견! GID 재생성 중...");
            // 중복이 발견되면 재귀 호출 후 반환된 값을 다시 반환
            return randomGid();
        }
        System.out.println("GID 발급 완료: " + gid);
        return gid;
    }

    /**
     * 그룹 존재 여부 검사 및 반환
     * @param gid
     * @return Group (or Null)
     * @TODO: groupManagement로 옮기기
     */
    public Group EnableGroup(String gid) {
        // gid 유효성 검사
        if (!groupRepository.existsByGid(gid)) {
            return null;
        } else {
            return groupRepository.findByGid(gid);
        }
    }

    /**
     * 그룹 활성화 상태 변경하기
     * @param gid
     * @param updateState
     * @TODO: groupManagement로 옮기기
     */
    public void groupStateUpdate(String gid, Boolean updateState){
        // 그룹 활성화 신호가 참이면 DB에도 참으로 반영하기
        if (updateState) {
            groupRepository.updateEnableStateTrue(gid);
        }
        // 그룹 활성화 신호가 거짓이면, DB에도 거짓으로 반영
        else {
            groupRepository.updateEnableStateFalse(gid);
        }
    }
}
