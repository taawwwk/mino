package cclab.mino.common;

import cclab.mino.group.domain.ActivityArea;
import cclab.mino.group.domain.Group;
import cclab.mino.group.repository.GroupRepository;

import java.util.List;

public class GroupManagement {
    final GroupRepository groupRepository;

    public GroupManagement(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    // gid로 그룹 객체를 찾아줌
    public Group findGroup(String gid) {
        // gid 유효성 검사
        if (!groupRepository.existsByGid(gid)) {
            return null;
        } else {
            return groupRepository.findByGid(gid);
        }
    }

    // 그룹 수용 인원 확인하기
    public int getGroupCapacity(Group group){
        System.out.println("그룹 수용 인원"+group.getCapacity());
        return group.getCapacity();
    }

    // 그룹 수용 인원, 현재 인원 수 비교하기
    public Boolean compareCount(int groupCapacity, int currentUsersList){
        // 그룹에서 설정한 인원과 현재 참여한 인원이 같을 때 참으로 변경
        if (groupCapacity == currentUsersList){
            return true;
        }
        // 그룹에서 설정한 인원과 다르면 거짓으로 변경
        else{
            return false;
        }
    }
}
