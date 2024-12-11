package cclab.mino.common;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;

public class PointConverter {
    private static final GeometryFactory geometryFactory = new GeometryFactory();

    public static Point parseStringToPoint(String pointStr) {
        String[] parts = pointStr.replace("Point(lat=", "").replace(" lng=", "").replace(")", "").split(",");
        double lat = Double.parseDouble(parts[0]);
        double lng = Double.parseDouble(parts[1]);
        return geometryFactory.createPoint(new Coordinate(lng, lat)); // PostGIS Point는 (longitude, latitude)
    }

    public static String parseStringToPointString(String pointString) {
        // "Point(lat=36.54574942904744, lng=128.79136439412832)" 형태의 문자열을 파싱하여
        // "POINT(128.79136439412832 36.54574942904744)" 형태로 변환
        pointString = pointString.replace("Point(", "").replace(")", "");
        String[] latLng = pointString.split(", ");
        String lat = latLng[0].split("=")[1];
        String lng = latLng[1].split("=")[1];
        return "POINT(" + lng + " " + lat + ")";
    }


}