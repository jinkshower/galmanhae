package hiyen.galmanhae;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class GalmanhaeApplication {

    public static void main(String[] args) {
        SpringApplication.run(GalmanhaeApplication.class, args);
    }

}
