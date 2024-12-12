package cclab.mino.dispatch.controller;

import cclab.mino.common.response.Message;
import cclab.mino.common.response.Response;
import cclab.mino.dispatch.domain.Dispatch;
import cclab.mino.dispatch.dto.CheckDispatchDTO;
import cclab.mino.dispatch.dto.SetDispatchReqDTO;
import cclab.mino.dispatch.service.DispatchService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("dispatch")
public class DispatchController {
    private final DispatchService dispatchService;

    public DispatchController(DispatchService dispatchService) {
        this.dispatchService = dispatchService;
    }

    /**
     * 출동 신호 설정 API
     * @param setDispatchReqDTO
     * @return
     */
    @PostMapping("/set")
    public ResponseEntity<?> setDispatch(@RequestBody SetDispatchReqDTO setDispatchReqDTO){
        /*
         * ---- 출동 신호 설정 로직 ----
         * 1. req. body의 senior id, manager id를 가져옴. -> DTO의 데이터 가져오기
         * 2. sen., man.이 존재하는지 확인 ->  user management 모듈에 이게 있나?
         * 2-1. 존재하지 않는다면 예외처리.
         * 3. 존재하다면 DB에 행 하나 추가 -> 서비스 레이어의 repository.save()
         * 3-1. 행 추가할 때, user 테이블의 uid를 잘 참조하는지 확인해야 함. ->
         * 4. 행 추가하고 dispatch 상태를 True로 업데이트
         * */

        long seniorUid = setDispatchReqDTO.getSeniorUid(); // 노인 uid
        long managerUid = setDispatchReqDTO.getManagerUid(); // 복지사 uid
        String gid = setDispatchReqDTO.getGid(); // 그룹 id

        // 존재하는 노인, 복지사인지 확인 + 두 유저가 같은 소속의 그룹인지 확인
        if (!dispatchService.enableDispatch(seniorUid, managerUid)){
            return ResponseEntity.ok().body(Message.builder()
                    .status(Response.StatusEnum.INTERNAL_SERVER_ERROR)
                    .message("출동 신호 설정 실패")
                    .data(null)
                    .build());
        }

        // 출동 신호 설정
        dispatchService.setDispatchSign(gid, seniorUid, managerUid);

        return ResponseEntity.ok().body(Message.builder()
                 .status(Response.StatusEnum.OK)
                 .message("출동 신호 설정 완료")
                 .data(null)
                 .build());
    }

    /**
     * 출동 신호 조회 API
     * @param gid
     * @return
     */
    @GetMapping("/check")
    public ResponseEntity<?> checkDispatch(@RequestParam String gid){

        // 출동 신호 이력 여부 검사
        Dispatch dispatch = dispatchService.checkDispatchSign(gid);

        // 출동 신호 조회 후 반환하기
        if(dispatch != null){
            return ResponseEntity.ok().body(Message.builder()
                    .status(Response.StatusEnum.OK)
                    .message("그룹의 현재 출동신호를 조회합니다.")
                    .data(
                            CheckDispatchDTO.builder()
                                    .seniorUid(dispatch.getSenior().getId())
                                    .managerUid(dispatch.getManager().getId())
                                    .build()
                    )
                    .build());
        }
        return ResponseEntity.ok().body(Message.builder()
                .status(Response.StatusEnum.OK)
                .message("출동 신호가 존재하지 않습니다.")
                .data(null)
                .build());
    }
}
