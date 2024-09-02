package bg.duosoft.uniapplicationdemo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("http://localhost:8080");
        config.getAllowedOrigins().addAll(List.of(
                "http://localhost:5173",
                "http://localhost:5174",
                "http://192.168.0.103:5173",
                "http://92.247.126.131:5173",
                "http://92.247.126.131:8080",
                "https://uni-application.ddns.net"));
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        config.addExposedHeader("Content-Disposition");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
