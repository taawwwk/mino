package cclab.mino.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
public class ManagersDTO{
    private long uid;
    @JsonProperty("mName")
    private String managerName;
    @JsonProperty("mTel")
    private String managerTel;
}