package cclab.mino.group.controller;

import cclab.mino.common.response.Message;
import cclab.mino.common.response.Response;
import cclab.mino.group.domain.Group;
import cclab.mino.group.dto.*;
import cclab.mino.group.service.GroupService;
import cclab.mino.user.dto.ManagersDTO;
import cclab.mino.user.dto.UserDTO;
import cclab.mino.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;


@RestController
@RequestMapping("group")
public class GroupController {
    private final GroupService groupService;
    private final UserService userService;

    public GroupController(GroupService groupService, UserService userService) {
        this.groupService = groupService;
        this.userService = userService;
    }

    /**
     * 그룹 생성 API
     * @Method POST
     * @param requestDTO
     * @return
     */
    @PostMapping("/create")
    public ResponseEntity<?> createGroup(@RequestBody GroupRequestDTO requestDTO){
        try{
            GroupResponseDTO responseDTO = groupService.generateGroup(requestDTO);


            return ResponseEntity.ok().body(
                    Message.builder()
                    .status(Response.StatusEnum.OK)
                    .message("GID가 성공적으로 발급되었습니다.")
                    .data(responseDTO)
                    .build());
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    /**
     * 그룹 상태 확인 API
     * @Method: Get
     * @param gid
     * @return
     */
    @GetMapping("/state")
    public ResponseEntity<?> checkGroupStatus(@RequestParam String gid){

        Group group = groupService.EnableGroup(gid);
        Boolean groupState = group.getEnableState();
        System.out.println("그룹 활성화 단계"+groupState);

        // 그룹이 존재하지 않으면, null
        if (groupState == null){
            return ResponseEntity.ok().body(
                    Message.builder()
                            .status(Response.StatusEnum.OK)
                            .message("그룹 활성화 단계 확인에 오류가 발생했습니다.")
                            .data(StateResponseDTO.builder()
                                    .groupState(groupState)
                                    .build())
                            .build());
        }

        // groupService.groupStateUpdate(gid, true); -> 왜 주석?
        // Stirng인 gid를 long으로 변환


        long gidInt = Long.valueOf(gid);
        System.out.println("gid: " + gidInt);

        // 그룹에 소속된 모든 유저 다 가져오기
        UserDTO userDTO = userService.getUsers(group.getId());

        // 모든 유저 중, manager만 가져오기
        ArrayList<ManagersDTO> managerArr = userDTO.getManagers();

        return ResponseEntity.ok().body(
                Message.builder()
                        .status(Response.StatusEnum.OK)
                        .message("그룹 활성화 단계를 확인합니다.")
                        .data(StateResponseDTO.builder()
                                .groupState(groupState)
                                .managers(managerArr)
                                .build())
                        .build());
    }

    // TODO: 그룹 삭제, 그룹 서비스 시간 연장하기 기능 구현하기
}
