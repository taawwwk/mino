package cclab.mino.location.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.boot.BootLogging;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ManagerRepoertResponseDTO {
    private Boolean groupStatus; // 그룹 운영 상태.
    private Boolean gatherProgress; // 소속 그룹의 집결 진행 상홍
}
