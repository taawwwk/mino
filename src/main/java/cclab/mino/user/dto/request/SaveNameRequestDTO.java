package cclab.mino.user.dto.request;

import cclab.mino.user.dto.SeniorsDTO;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;

@Getter
@Builder
public class SaveNameRequestDTO {
    private String gid;
    private ArrayList<SeniorsDTO> seniors;
}
