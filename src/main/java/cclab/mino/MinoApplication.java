package cclab.mino;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
public class MinoApplication {
    public static void main(String[] args) {
        SpringApplication.run(MinoApplication.class, args);
    }
}
