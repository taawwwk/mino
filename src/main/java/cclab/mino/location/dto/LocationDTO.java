package cclab.mino.location.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
public class LocationDTO {
    private double latitude; // 위도
    private double longitude; // 경도

}
