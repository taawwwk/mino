package cclab.mino.location.dto.response;

import cclab.mino.location.dto.LocationManagerDTO;
import cclab.mino.location.dto.LocationSeniorDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;

@Getter
@Builder
@AllArgsConstructor
public class UsersLocationResponseDTO {
    private String gid;
    private ArrayList<LocationSeniorDTO> seniors;
    private ArrayList<LocationManagerDTO> managers;
}
