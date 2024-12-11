package cclab.mino.location.controller;

import cclab.mino.common.response.Message;
import cclab.mino.common.response.Response;
import cclab.mino.location.dto.LocationDTO;
import cclab.mino.location.dto.request.LocationReportRequestDTO;
import cclab.mino.location.dto.request.LocationReportSeniorRequestDTO;
import cclab.mino.location.dto.response.ManagerRepoertResponseDTO;
import cclab.mino.location.dto.response.SeniorStatusResponseDTO;
import cclab.mino.location.dto.response.UsersLocationResponseDTO;
import cclab.mino.location.service.LocationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("location")
public class LocationController {
    private final LocationService locationService;

    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    /**
     * <b>Location report API for managers</b>
     * @param locationReportRequestDTO -> uid, rid, location(lat, long)
     * @return groupStatus: 그룹의 위치 모니터링 활성화 여부, gatherProgress: 그룹의 집결 기능 동작 여부
     */
    @PostMapping("/report/manager")
    public ResponseEntity<?> reportLocation(@RequestBody LocationReportRequestDTO locationReportRequestDTO){
        // 1. ReqDTO에서 uid와 rid를 추출
        long uid = locationReportRequestDTO.getUid();
        int rid = locationReportRequestDTO.getRid(); // -> rid 어디에 쓰이는지?

        // 2. 요청 바디에서 위치정보(위도,경도) 추출하고 객체 생성
        LocationDTO locationDTO = locationReportRequestDTO.getLocation();
        double latitude = locationDTO.getLatitude();
        double longitude = locationDTO.getLongitude();

        LocationDTO location = LocationDTO.builder()
                .latitude(latitude)
                .longitude(longitude)
                .build();

        // 4. location 테이블에 해당 uid 유저의 위치 정보가 존재 여부 검사 -> EXIST
        // 4-1. 위치 정보 보고 기록이 없다면 INSERT
        // 4-2. 이미 위치 정보가 존재한다면 UPDATE

        // 해당 유저가 위치 보고 이력이 존재하는지 검사.
        // TODO: rid가 일치해야 저장or수정되게 해야 함.
        Boolean updateOrInsert = locationService.findLocationHistory(uid);

        if(!updateOrInsert) {
            // 만약 위치 보고 이력이 없다면, save(=Insert)
            locationService.saveUserLocation(uid, location);
        }
        else{
            // 위치 보고 이력이 있다면 Update
            locationService.updateManagerLocation(uid, location);
        }

        // 현재 그룹의 운영 및 집결 상태 반환.
        // TODO: 그룹 정보를 가져와서 직접 데이터를 만들어줘야 함. groupStatus는 그룹의 존재여부, gatherProgress 집결 상태. 추후 구현 예정
        ManagerRepoertResponseDTO managerRepoertResponseDTO =
                ManagerRepoertResponseDTO.builder()
                        .groupStatus(true)
                        .gatherProgress(false)
                        .build();

        return ResponseEntity.ok().body(Message.builder()
                .status(Response.StatusEnum.OK)
                .message("위치 보고가 정상적으로 이루어짐")
                .data(managerRepoertResponseDTO)
                .build());
        /**
         *----- 위치 보고 로직 -----
         * 1. ReqDTO에서 uid와 rid를 추출
         * 2. uid와 rid의 유효성 검사. User 테이블에서 EXIST
         * 3. 만약 uid가 없다면 '존재하지 않는 유저입니다.'
         * 3-1. 만약 rid가 일치하지 않다면 '유효하지 않은 유저입니다.'
         * 4. location 테이블에 해당 uid 유저의 위치 정보가 존재 여부 검사 -> EXIST
         * 4-1. 위치 정보 보고 기록이 없다면 INSERT
         * 4-2. 이미 위치 정보가 존재한다면 UPDATE
         * 5. 위치 저장 후 그룹 활성화 상태 반환.
         * 5-1. Location.uid = User.uid -> User.gid = Group.gid -> 그룹 아이디 찾고 활성화 상태 찾아서 반환
         * 5-2. 아니면 요청 양식에 gid도 보내라하기
         */


    }

    /**
     * <i>Location report API for senior</i>
     * @param locationReportSeniorRequestDTO (uid, rid, Location(Lat and Long), healthStatus)
     * @return SeniorStatus (deviationStatus, dangerStatus, gatherStatus, gatherProgress)
     */
    @PostMapping("/report/senior")
    public ResponseEntity<?> reportManagerLocation(@RequestBody LocationReportSeniorRequestDTO locationReportSeniorRequestDTO){
        /**
         *  <b>위치 보고 로직</b>
         1. ReqDTO에서 uid와 rid를 추출
         2. uid와 rid의 유효성 검사. User 테이블에서 EXIST
         3. 만약 uid가 없다면 '존재하지 않는 유저입니다.'
         3-1. 만약 rid가 일치하지 않다면 '유효하지 않은 유저입니다.'
         4. location 테이블에 해당 uid 유저의 위치 정보가 존재 여부 검사 -> EXIST
         4-1. 위치 정보 보고 기록이 없다면 INSERT
         4-2. 이미 위치 정보가 존재한다면 UPDATE
         5. 위치 저장/업데이트 후 노인의 상태(seniorStatus) 업데이트 --> 이건 Status 모듈에서 담당
         6. SeniorStatus를 응답
         **/

        // 1. ReqDTO에서 uid와 rid를 추출
        long uid = locationReportSeniorRequestDTO.getUid();
        int rid = locationReportSeniorRequestDTO.getRid();

        // 2. 요청 바디에서 위치정보(위도,경도) 추출하고 객체 생성
        LocationDTO locationDTO = locationReportSeniorRequestDTO.getLocation();
        double latitude = locationDTO.getLatitude();
        double longitude = locationDTO.getLongitude();

        LocationDTO location = LocationDTO.builder()
                .latitude(latitude)
                .longitude(longitude)
                .build();

        // 3. 건강 상태 추출하기
        int healthStatus = locationReportSeniorRequestDTO.getHealthStatus();

        // 4. location 테이블에 해당 uid 유저의 위치 정보가 존재 여부 검사 -> EXIST
        // 4-1. 위치 정보 보고 기록이 없다면 INSERT
        // 4-2. 이미 위치 정보가 존재한다면 UPDATE

        // 위치 보고 이력 조회
        Boolean updateOrInsert = locationService.findLocationHistory(uid);

        if(!updateOrInsert) {
            locationService.saveUserLocation(uid, location);
            // 2. uid와 rid의 유효성 검사. User 테이블에서 EXIST.
            // 2번 어디? 없나?
        }
        else{
            locationService.updateSeniorLocation(uid, location, healthStatus);
        }

        SeniorStatusResponseDTO seniorStatus = locationService.selectSeniorStatus(uid);

        return ResponseEntity.ok().body(Message.builder()
                .status(Response.StatusEnum.OK)
                .message("위치 보고가 정상적으로 이루어짐")
                .data(seniorStatus)
                .build());
    }

    /**
     *
     * @param gid
     * @return
     */
    @GetMapping("/users")
    public ResponseEntity<?> sendSeniorsLocation(@RequestParam String gid){
        UsersLocationResponseDTO responseDTO =
                locationService.selectUsersLocation(gid);

        return ResponseEntity.ok().body(Message.builder()
                .status(Response.StatusEnum.OK)
                .message("테스트 중")
                .data(responseDTO)
                .build());
    }
}