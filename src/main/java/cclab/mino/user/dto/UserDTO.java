package cclab.mino.user.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;

@Builder
@Getter
public class UserDTO {
    private ArrayList<SeniorsDTO> seniors;
    private ArrayList<ManagersDTO> managers;
}
