package cclab.mino.user.dto.response;

import cclab.mino.group.domain.ActivityArea;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class JoinResponseDTO {
    private Long uid;
    private List<String> activityArea;
    private List<List<String>> dangerArea;
}
