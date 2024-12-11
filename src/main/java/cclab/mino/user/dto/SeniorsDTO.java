package cclab.mino.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SeniorsDTO {
    private long uid;
    @JsonProperty("sName")
    private String seniorName;
}
