package cclab.mino.dispatch.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CheckDispatchDTO {
    private long seniorUid;
    private long managerUid;
}
