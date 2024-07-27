package bg.duosoft.uniapplicationdemo;

import io.github.cdimascio.dotenv.Dotenv;
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
        Dotenv dotenv = Dotenv.configure()
                .load();

        // Set system properties or environment variables
        System.setProperty("MAIL_SMTP_USERNAME", dotenv.get("MAIL_SMTP_USERNAME"));
        System.setProperty("MAIL_SMTP_PASSWORD", dotenv.get("MAIL_SMTP_PASSWORD"));
        System.setProperty("DATASOURCE_USERNAME", dotenv.get("DATASOURCE_USERNAME"));
        System.setProperty("DATASOURCE_PASSWORD", dotenv.get("DATASOURCE_PASSWORD"));
        System.setProperty("KEYCLOAK_REALM", dotenv.get("KEYCLOAK_REALM"));
        System.setProperty("KEYCLOAK_CREDENTIALS_SECRET", dotenv.get("KEYCLOAK_CREDENTIALS_SECRET"));
        SpringApplication.run(UniApplicationDemoApplication.class, args);
    }
}
