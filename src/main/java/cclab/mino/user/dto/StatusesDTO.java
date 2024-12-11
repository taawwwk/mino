package cclab.mino.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class StatusesDTO {
    private int healthStatus;
    private int dangerStatus;
    private int deviationStatus;
    private int gatherStatus;
}
