package cclab.mino.user.dto.request;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class JoinAsManagersRequestDTO {
    private String gid;
    private String mName;
    private String mTel;
}
