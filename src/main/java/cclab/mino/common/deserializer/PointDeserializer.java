package cclab.mino.common.deserializer;

import cclab.mino.common.custom.Point;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class PointDeserializer extends JsonDeserializer<Point> {

    @Override
    public Point deserialize(JsonParser jsonParser, DeserializationContext ctxt) throws IOException {
        String text = jsonParser.getText();
        // 문자열을 파싱하여 Point 객체 생성
        if (text.startsWith("Point(") && text.endsWith(")")) {
            String[] parts = text.substring(6, text.length() - 1).split(", ");
            double lat = Double.parseDouble(parts[0].split("=")[1]);
            double lng = Double.parseDouble(parts[1].split("=")[1]);
            return new Point(lat, lng);
        } else {
            throw new IOException("Cannot parse Point: " + text);
        }
    }
}
