package cclab.mino.dispatch.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class SetDispatchReqDTO {
    private String gid;
    private Long seniorUid;
    private Long managerUid;
}
