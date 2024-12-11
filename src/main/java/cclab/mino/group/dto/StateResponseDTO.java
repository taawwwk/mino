package cclab.mino.group.dto;

import cclab.mino.user.dto.ManagersDTO;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;

@Builder
@Getter
public class StateResponseDTO {
    private Boolean groupState;
    private ArrayList<ManagersDTO> managers;
}
