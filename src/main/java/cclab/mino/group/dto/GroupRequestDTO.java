package cclab.mino.group.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
public class GroupRequestDTO {
    private String name;
    private String org;
    private String tel;
    private int capacity;
    private List<String> activityArea;
    private List<List<String>> dangerArea;

    public String getName() {
        return name;
    }
    public String getOrg() {
        return org;
    }
    public String getTel() {
        return tel;
    }
    public int getCapacity() {
        return capacity;
    }
    public List<String> getActivityArea() {
        return activityArea;
    }
    public List<List<String>> getDangerArea() {
        return dangerArea;
    }
}
