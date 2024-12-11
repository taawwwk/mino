package cclab.mino.common;

import cclab.mino.user.domain.User;
import cclab.mino.user.dto.ManagersDTO;
import cclab.mino.user.dto.SeniorsDTO;
import cclab.mino.user.dto.UserDTO;
import cclab.mino.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

public class UserManagement {
    private final UserRepository userRepository;

    public UserManagement(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // 유저 찾기
    public User findUser(long uid){
        User user = userRepository.findUserById(uid);
        if (user == null){
            return null;
        }
        return user;
    }

/*    public Senior findSenior(User user){
        Senior senior = seniorRepository.findSeniorByUser(user);
        if (senior == null){
            return null;
        }
        return senior;
    }

    public Manager findManager(User user){
        Manager manager = managerRepository.findManagerByUser(user);
        if (manager == null){
            return null;
        }
        return manager;
    }*/

    public UserDTO getUsers(long gid){
        // gid와
        List<User> users= userRepository.findAllByGroupId(gid);

        ArrayList<SeniorsDTO> seniorArr = new ArrayList<>();
        ArrayList<ManagersDTO> managerArr = new ArrayList<>();

        for (int i = 0; i<users.size(); i++){
            if (users.get(i).getRid() == 0) {
                seniorArr.add(
                        SeniorsDTO.builder()
                                .uid(users.get(i).getId())
                                .seniorName(users.get(i).getUserName())
                                .build()
                );
            }

            else if (users.get(i).getRid() == 1){
                managerArr.add(
                        ManagersDTO.builder()
                                .uid(users.get(i).getId())
                                .managerName(users.get(i).getUserName())
                                .build()
                );
            }
        }
        return UserDTO.builder()
                .seniors(seniorArr)
                .managers(managerArr)
                .build();
    }
}
