package cclab.mino.location.dto;

import cclab.mino.user.dto.StatusesDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class LocationSeniorDTO {
    private long uid;
    @JsonProperty("sName")
    private String seniorName;
    private LocationDTO location;
    private StatusesDTO seniorStatus;
}
