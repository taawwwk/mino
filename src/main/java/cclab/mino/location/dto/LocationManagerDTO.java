package cclab.mino.location.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class LocationManagerDTO {
    private long uid;
    @JsonProperty("mName")
    private String managerName;
    private LocationDTO location;
}
