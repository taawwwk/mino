package cclab.mino.location.service;

import cclab.mino.common.UserManagement;
import cclab.mino.group.domain.Group;
import cclab.mino.group.repository.GroupRepository;
import cclab.mino.location.domain.Locations;
import cclab.mino.location.dto.LocationDTO;
import cclab.mino.location.dto.LocationManagerDTO;
import cclab.mino.location.dto.LocationSeniorDTO;
import cclab.mino.location.dto.response.SeniorStatusResponseDTO;
import cclab.mino.location.dto.response.UsersLocationResponseDTO;
import cclab.mino.location.repository.LocationRepository;
import cclab.mino.user.domain.Statuses;
import cclab.mino.user.domain.User;
import cclab.mino.user.dto.ManagersDTO;
import cclab.mino.user.dto.SeniorsDTO;
import cclab.mino.user.dto.StatusesDTO;
import cclab.mino.user.dto.UserDTO;
import cclab.mino.user.repository.StatusesRepostiory;
import cclab.mino.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Service
public class LocationService {
    private final UserRepository userRepository;
    private final LocationRepository locationRepository;
    private final GroupRepository groupRepository;
    private final StatusesRepostiory statusesRepostiory;

    public LocationService(UserRepository userRepository, LocationRepository locationRepository, GroupRepository groupRepository, StatusesRepostiory statusesRepostiory) {
        this.userRepository = userRepository;
        this.locationRepository = locationRepository;
        this.groupRepository = groupRepository;
        this.statusesRepostiory = statusesRepostiory;
    }

    /**
     * 위치 보고 이력 조회하기.
     * @param uid
     * @return
     */
    @Transactional
    public Boolean findLocationHistory(long uid){
        UserManagement userManagement = new UserManagement(userRepository);

        // uid와 일치하는 유저 생성
        User user = userManagement.findUser(uid);

        // location 테이블에 해당 user의 위치 정보가 존재한다면 update
        if(locationRepository.existsByUser(user)){
            System.out.println("위치를 업데이트합니다.");
            return true;
        }
        // location 테이블에 해당 user의 위치 정보가 존재하지 않는다면 save
        else{
            System.out.println("위치를 저장합니다.");
            return false;
        }
    }

    /**
     * User의 위치 저장.
     * @param uid
     * @param locationDTO
     */
    @Transactional
    public void saveUserLocation(long uid, LocationDTO locationDTO){
        UserManagement userManagement =
                new UserManagement(userRepository);
        User user = userManagement.findUser(uid);

        Locations locations = Locations.builder()
                .user(user)
                .latitude(locationDTO.getLatitude())
                .longitude(locationDTO.getLongitude())
                .build();

        System.out.println("***************");
        System.out.println("유저 ID: " + user.getId());
        System.out.println("유저 이름: "+ user.getUserName());
        System.out.println("위도: " + locations.getLatitude() + ", 경도: " + locations.getLongitude());
        System.out.println("저장되었습니다.");
        System.out.println("***************");

        locationRepository.save(locations);

    }

    /**
     * 노인의 상태 업데이트 -> 아직 사용하진 않을듯? 곧 사용 예정
     * @param uid
     * @param statusesDTO
     */

    public void updateSeniorsStatuses(long uid, StatusesDTO statusesDTO){
        // uid와 일치하는 유저 찾기
        UserManagement userManagement =
                new UserManagement(userRepository);
        User user = userManagement.findUser(uid);

        // 일치하는 user의 상태 찾고. statusDTO로 status 객체 빌더
        Statuses statuses = Statuses.builder()
                .user(user)
                .healthStatus(statusesDTO.getHealthStatus())
                .dangerStatus(statusesDTO.getDangerStatus())
                .deviationStatus(statusesDTO.getDeviationStatus())
                .gatherStatus(statusesDTO.getGatherStatus())
                .build();

        // 상태 업데이트
        statusesRepostiory.updateStatusesByUser(uid,
                statuses.getHealthStatus(),
                statuses.getDangerStatus(),
                statuses.getDeviationStatus(),
                statuses.getDeviationStatus());
    }

    /**
     * 노인의 상태 찾아서 반환
     * @param uid
     * @return
     */
    @Transactional
    public SeniorStatusResponseDTO selectSeniorStatus(long uid){
        UserManagement userManagement =
                new UserManagement(userRepository);

        User user = userManagement.findUser(uid);

        Statuses statuses = statusesRepostiory.findByUser(user);
        // TODO: 그룹의 집결 상태를 가져오는 코드 구현하기 -> 추후 집결 파트 진행 할 때 수정해야 함.

        return SeniorStatusResponseDTO.builder()
                .dangerStatus(statuses.getDangerStatus())
                .deviationStatus(statuses.getDeviationStatus())
                .gatherStatus(statuses.getGatherStatus())
                .gatherProgress(false)
                .build();
    }

    /**
     * 노인 위치 정보 업데이트
     * @param uid
     * @param locationDTO
     * @param healthStatus
     */
    @Transactional
    public void updateSeniorLocation(long uid, LocationDTO locationDTO, int healthStatus){
        UserManagement userManagement =
                new UserManagement(userRepository);

        // uid로 유저 찾기
        User user = userManagement.findUser(uid);

        // location 업데이트를 위한 객체 빌더
        Locations locations = Locations.builder()
                .user(user)
                .latitude(locationDTO.getLatitude())
                .longitude(locationDTO.getLongitude())
                .build();

        System.out.println("***************");
        System.out.println("유저 ID: " + user.getId());
        System.out.println("유저 이름: "+ user.getUserName());
        System.out.println("위도: " + locations.getLatitude() + ", 경도: " + locations.getLongitude());
        System.out.println("변경되었습니다.");
        System.out.println("***************");

        // 변경된 위치 정보를 바탕으로 업데이트하기.
        locationRepository.updateLocation(user, locations.getLatitude(), locationDTO.getLongitude());

        // 노인의 현재 건강 상태 조회
        Statuses currentStatuses = statusesRepostiory.findByUser(user);
        int currentHealthStatus = currentStatuses.getHealthStatus();

        // 위치 정보와 함께 보내는 건강 상태를 업데이트
        if(currentHealthStatus != healthStatus){
            statusesRepostiory.updateHealthStatusByUser(user.getId(),healthStatus);
        }
    }

    /**
     * 복지사 위치 업데이트
     * @param uid
     * @param locationDTO
     */
    public void updateManagerLocation(long uid, LocationDTO locationDTO){
        UserManagement userManagement =
                new UserManagement(userRepository);

        // uid로 유저 찾기
        User user = userManagement.findUser(uid);

        // location 업데이트를 위한 객체 빌더
        Locations locations = Locations.builder()
                .user(user)
                .latitude(locationDTO.getLatitude())
                .longitude(locationDTO.getLongitude())
                .build();

        System.out.println("***************");
        System.out.println("유저 ID: " + user.getId());
        System.out.println("유저 이름: "+ user.getUserName());
        System.out.println("위도: " + locations.getLatitude() + ", 경도: " + locations.getLongitude());
        System.out.println("변경되었습니다.");
        System.out.println("***************");

        // 변경된 위치 정보를 바탕으로 업데이트하기.
        locationRepository.updateLocation(user, locations.getLatitude(), locationDTO.getLongitude());

    }

    /**
     * 그룹 유저들의 위치 정보 배열 가져오기
     * @param gid
     * @return
     */
    // TODO: 노인, 복지사 위치 정보 같음
    @Transactional
    public UsersLocationResponseDTO selectUsersLocation(String gid){
        /*
         * 노인 위치 정보 요청 알고리즘
         * 1. gid와 일치하는 유저(노인) 검색 후 uid 추출 -> UserService.getUser();
         * 2. location DB에 uid와 일치하는 행 가져오기 -> locationRepository.findAllByUid();
         * 3. 위도, 경도를 locationDTO로 만들고 senior 객체에 uid, location로 담기
         * 4. 3을 유저 수 만큼 반복해서 senior 객체를 배열로 만들기
         * 5. seniorStatus는 null로?
         */

        // 그룹에 소속된 유저 정보 가져오기
        Group group = groupRepository.findByGid(gid);
        ArrayList<Locations> locationsArrayList = new ArrayList<>();
        long groupId = group.getId();

        ArrayList<LocationSeniorDTO> seniors = new ArrayList<>();
        ArrayList<LocationManagerDTO> managers = new ArrayList<>();

        UserManagement userManagement = new UserManagement(userRepository);
        UserDTO users = userManagement.getUsers(groupId);

        ArrayList<SeniorsDTO> seniorsDTO = users.getSeniors();

        ArrayList<ManagersDTO> managersDTO = users.getManagers();

        // 유저 목록에서 노인만 찾기
        for (int i=0; i<seniorsDTO.size(); i++){
            // 노인 id 추출하기
            long seniorUid = seniorsDTO.get(i).getUid();
            String seniorName = seniorsDTO.get(i).getSeniorName();

            // 노인 아이디로 유저 객체 찾기
            User user = userRepository.findUserById(seniorUid);
            System.out.println("유저 id: " +user.getId());

            Statuses statuses = statusesRepostiory.findByUser(user);
            StatusesDTO statusesDTO = StatusesDTO.builder()
                    .healthStatus(statuses.getHealthStatus())
                    .dangerStatus(statuses.getDangerStatus())
                    .deviationStatus(statuses.getDeviationStatus())
                    .gatherStatus(statuses.getDeviationStatus())
                    .build();

            // 유저 객체의 아이디를 추출하고 유저의 위치정보 가져오기
            locationsArrayList.add(locationRepository.findByUser(user));
            System.out.println("유저 위치: " + locationsArrayList.get(i));

            // 가져온 유저 위치 정보로 위도경도 DTO 만들기
            LocationDTO sLocation = LocationDTO.builder()
                    .latitude(locationsArrayList.get(i).getLatitude())
                    .longitude(locationsArrayList.get(i).getLongitude())
                    .build();

            // 노인의 id, 위치 객체로 senior 어레이리스트 만들기
            seniors.add(LocationSeniorDTO.builder()
                    .uid(seniorUid)
                    .seniorName(seniorName)
                    .location(sLocation)
                    .seniorStatus(statusesDTO)
                    .build());
        }


        // 유저 목록에서 복지사만 찾기
        for (int i=0; i<managersDTO.size(); i++){
            // 복지사 id 추출하기
            long managerUid = managersDTO.get(i).getUid();
            String managerName = managersDTO.get(i).getManagerName();

            // 복지사 아이디로 유저 객체 찾기
            User user = userRepository.findUserById(managerUid);
            System.out.println("유저 id: " +user.getId());

            // 유저 객체의 아이디를 추출하고 유저의 위치정보 가져오기
            locationsArrayList.add(locationRepository.findByUser(user));
            System.out.println("유저 위치: " + locationsArrayList.get(i));

            // 가져온 유저 위치 정보로 위도경도 DTO 만들기
            LocationDTO mLocation = LocationDTO.builder()
                    .latitude(locationsArrayList.get(i).getLatitude())
                    .longitude(locationsArrayList.get(i).getLongitude())
                    .build();

            // 노인의 id, LocationDTO를 이용해 senior ArrayList 만들기
            managers.add(LocationManagerDTO.builder()
                    .uid(managerUid)
                    .managerName(managerName)
                    .location(mLocation)
                    .build());
        }

        // gid, 반복문 안에서 생성한 노인 객체 배열로 응답 객체 만들기
        UsersLocationResponseDTO responseDTO = UsersLocationResponseDTO.builder()
                .gid(gid)
                .seniors(seniors)
                .managers(managers)
                .build();

        return responseDTO;
    }
}