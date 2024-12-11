package cclab.mino.common.custom;

import cclab.mino.common.deserializer.PointDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

// *********************NOT USED********************* //
@JsonDeserialize(using = PointDeserializer.class)
public class Point{
    private double lat;
    private double lng;

    // 기본 생성자
    public Point() {}

    // 파라미터 생성자
    public Point(double lat, double lng) {
        this.lat = lat;
        this.lng = lng;
    }

    // Getters and setters
    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    @Override
    public String toString() {
        return "Point(lat=" + lat + ", lng=" + lng + ")";
    }
}
