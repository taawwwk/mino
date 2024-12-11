package cclab.mino.common.deserializer;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import lombok.Getter;
import lombok.Setter;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.io.WKTReader;
/*import org.springframework.data.geo.Point;
import org.springframework.data.geo.Polygon;*/
import org.locationtech.jts.geom.Polygon;

import java.io.IOException;

@Getter
@Setter
// *********************NOT USED YET********************* //
public class PolygonDeserializer extends JsonDeserializer<Polygon> {
    private static final GeometryFactory geometryFactory = new GeometryFactory();
    @Override
    public Polygon deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
     /*   String rawText = jsonParser.getText();
        String[] pointStrings = rawText.replace("[", "").replace("]", "").split("\\),\\s*\\(");

        List<Point> points = new ArrayList<>();
        for(String pointString: pointStrings){
            String[] coordinates = pointString.split("\\s+");
            double x = Double.parseDouble(coordinates[0].replace("(", ""));
            double y = Double.parseDouble(coordinates[1].replace(")", ""));
            points.add(new Point(x, y));
        }

        return new Polygon(points);
    }*/

        String wkt = jsonParser.getText();
        WKTReader reader = new WKTReader(geometryFactory);
        try {
            return (Polygon) reader.read(wkt);
        } catch (Exception e) {
            throw new IOException("Failed to deserialize Polygon", e);
        }
    }
}
