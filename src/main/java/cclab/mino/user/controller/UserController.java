package cclab.mino.user.controller;

import cclab.mino.common.response.Message;
import cclab.mino.common.response.Response;
import cclab.mino.group.domain.ActivityArea;
import cclab.mino.group.domain.Group;
import cclab.mino.user.dto.*;
import cclab.mino.group.service.GroupService;
import cclab.mino.user.dto.request.JoinAsManagersRequestDTO;
import cclab.mino.user.dto.request.JoinAsSeniorsRequestDTO;
import cclab.mino.user.dto.request.SaveNameRequestDTO;
import cclab.mino.user.dto.response.JoinResponseDTO;
import cclab.mino.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("user")
public class UserController {

    private final GroupService groupService;
    private final UserService userService;

    public UserController(GroupService groupService, UserService userService) {
        this.groupService = groupService;
        this.userService = userService;
    }

    /**
     * 노인 그룹 참여
     * @method Post
     * @param joinAsSeniorsRequestDTO (gid: String)
     * @return uid: Long
     */
    @PostMapping("/join/seniors")
    public ResponseEntity<?> joinGroupAsSeniors(@RequestBody JoinAsSeniorsRequestDTO joinAsSeniorsRequestDTO){
        try{
            String gid = joinAsSeniorsRequestDTO.getGid();
            Group group = groupService.EnableGroup(gid);
            Boolean enable = userService.checkGroupCapacity(gid);
            if (group == null){

                return ResponseEntity.badRequest().body(Message.builder()
                        .status(Response.StatusEnum.BAD_REQUEST)
                        .message("유효하지 않은 GID 입니다.")
                        .data(null)
                        .build());
            }

            if(enable){
                return ResponseEntity.badRequest().body(Message.builder()
                        .status(Response.StatusEnum.BAD_REQUEST)
                        .message("더 이상 그룹에 참여할 수 없습니다.")
                        .data(null)
                        .build());
            }

            else {
                // uid 발급을 위해 서비스 레이어 메소드 사용
                Long uid = userService.issueUidforSenior(group);

                // 응답 객체 생성
                JoinResponseDTO joinResponseDTO = JoinResponseDTO.builder()
                        .uid(uid)
                        .build();

                return ResponseEntity.ok().body(Message.builder()
                        .status(Response.StatusEnum.OK)
                        .message("그룹 참여 성공")
                        .data(joinResponseDTO)
                        .build());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 복지사 그룹 참여
     * @Method: Post
     * @param joinAsManagersRequestDTO (gid: String, mName: String, mTel: String)
     * @return uid: Long
     */
    @PostMapping("join/managers")
    public ResponseEntity<?> joinGroupAsManagers(@RequestBody JoinAsManagersRequestDTO joinAsManagersRequestDTO){
        try{
            String gid = joinAsManagersRequestDTO.getGid();
            Group group = groupService.EnableGroup(gid);
            Boolean enable = userService.checkGroupCapacity(gid);
            String managerName = joinAsManagersRequestDTO.getMName();
            String managerTel = joinAsManagersRequestDTO.getMTel();

            if (group == null){

                return ResponseEntity.badRequest().body(Message.builder()
                        .status(Response.StatusEnum.BAD_REQUEST)
                        .message("유효하지 않은 GID 입니다.")
                        .data(null)
                        .build());
            }

            if(enable){
                return ResponseEntity.badRequest().body(Message.builder()
                        .status(Response.StatusEnum.BAD_REQUEST)
                        .message("더 이상 그룹에 참여할 수 없습니다.")
                        .data(null)
                        .build());
            }

            else {
                // 서비스 레이어로 gid를 넘김. 그룹에 소속시키고 uid 반환
                Long uid = userService.issueUidForManager(group, managerName, managerTel);

                // 그룹의 활동 영역 가져오기
                List<String> activityArea= userService.findActivityArea(group);
                // 그룹의 위험 영역 가져오기
                List<List<String>> dangerArea = userService.findDangerAreaArea(group);

                // 응답 객체 생성
                JoinResponseDTO joinResponseDTO = JoinResponseDTO.builder()
                        .uid(uid)
                        .activityArea(activityArea)
                        .dangerArea(dangerArea)
                        .build();

                return ResponseEntity.ok().body(Message.builder()
                        .status(Response.StatusEnum.OK)
                        .message("그룹 참여 성공")
                        .data(joinResponseDTO)
                        .build());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 그룹 현재원 목록 조회
     * @Method Get
     * @param gid: String
     * @return UserList: ArrayList
     */
    @GetMapping("/current")
    public ResponseEntity<?> currentUsers(@RequestParam String gid){
        // gid로 일치하는 그룹 가져오기
        Group group = groupService.EnableGroup(gid);

        // 그룹이 존재하지 않으면 조기 반환 후 종료
        if (group == null){
            return ResponseEntity.badRequest().body(Message.builder()
                    .status(Response.StatusEnum.BAD_REQUEST)
                    .message("일치하는 그룹이 없습니다.")
                    .data(null)
                    .build());
        }

        // 해당 그룹의 시리얼 id로 user 데이터 가져오기
        UserDTO userList = userService.getUsers(group.getId());

        return ResponseEntity.ok().body(Message.builder()
                .status(Response.StatusEnum.OK)
                .message("현재 인원 목록을 출력합니다.")
                .data(userList)
                .build());
    }

    /**
     * 그룹에 참여한 노인의 이름을 저장
     * @method Post
     * @param saveNameRequestDTO (gid: String, ArrayList: Seniors)
     * @return null
     */
    @PostMapping("/save")
    public ResponseEntity<?> saveSeniorList(@RequestBody SaveNameRequestDTO saveNameRequestDTO){

        // 그룹 수용인원과 현재 인원 비교
        Boolean enalbeState = userService.checkGroupCapacity(saveNameRequestDTO.getGid());

        // seniors가 비어있다네요 -> request에 맞춰 변수명 수정
        ArrayList<SeniorsDTO> seniors = saveNameRequestDTO.getSeniors();

        for(SeniorsDTO senior:seniors){
            System.out.println("유저 아디: " + senior.getUid());
            System.out.println("유저 이름: " + senior.getSeniorName());
        }

        if (enalbeState == false){
            // false로 바꾸기
            groupService.groupStateUpdate(saveNameRequestDTO.getGid(), enalbeState);

            // 아직 이름 저장 ㄴㄴ
            return ResponseEntity.ok().body(Message.builder()
                    .status(Response.StatusEnum.OK)
                    .message("그룹 정원과 일치하지 않아 저장할 수 없습니다.")
                    .data(null)
                    .build());
        }
        // 1. 노인 이름 저장하기
        userService.saveSeniorsName(seniors);

        // 2. 그룹 활성화 단계 true 바꾸기
        groupService.groupStateUpdate(saveNameRequestDTO.getGid(), true);

        return ResponseEntity.ok().body(Message.builder()
                .status(Response.StatusEnum.OK)
                .message("노인 이름을 저장하였습니다.")
                .data(null)
                .build());

    }

    /**
     * 그룹 탈퇴하기
     * @method Delete
     * @param uid: long, (TODO) gid로 한번 더 검사하기
     * @return null
     */
    @DeleteMapping("/join")
    public ResponseEntity<?> deleteUser(@RequestParam long uid){
        // uid와 일치하는 유저 행 삭제하기
        Boolean deleteState =  userService.deleteUser(uid);
        if (!deleteState){
            return ResponseEntity.badRequest().body(Message.builder()
                    .status(Response.StatusEnum.BAD_REQUEST)
                    .message("존재하지 않는 유저입니다")
                    .data(null)
                    .build());
        }
        return ResponseEntity.ok().body(Message.builder()
                .status(Response.StatusEnum.OK)
                .message("그룹에서 탈퇴했습니다.")
                .data(null)
                .build());
    }
}
