package bg.duosoft.uniapplicationdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "bg.duosoft.uniapplicationdemo.repositories")
@EnableFeignClients(basePackages = "bg.duosoft.uniapplicationdemo.services")
@EnableScheduling
public class UniApplicationDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(UniApplicationDemoApplication.class, args);
    }
}
