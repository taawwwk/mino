/*
package cclab.mino.config;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.locationtech.jts.geom.Polygon;
import cclab.mino.deserializer.PolygonDeserializer;

@Configuration
public class JacksonConfig {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Polygon.class, new PolygonDeserializer());
        objectMapper.registerModule(module);
        return objectMapper;
    }
}
*/
