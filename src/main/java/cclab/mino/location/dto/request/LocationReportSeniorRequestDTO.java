package cclab.mino.location.dto.request;

import cclab.mino.location.dto.LocationDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class LocationReportSeniorRequestDTO{
    private long uid;
    private int rid;
    private LocationDTO location;
    private int healthStatus;
}
