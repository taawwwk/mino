package cclab.mino.location.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
// 노인 위치 및 건강 상태 보고 시, 반환해주는 본인의 상태
public class SeniorStatusResponseDTO {
    private int deviationStatus;
    private int dangerStatus;
    private int gatherStatus;
    private Boolean gatherProgress;
}
