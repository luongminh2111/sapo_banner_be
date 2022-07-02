package vn.sapo.banner.config;

import java.util.Collections;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class WebConfiguration {

    @Bean
    public CorsFilter corsFilter() {
        var allowed = Collections.singletonList("*");
        var origins = List.of("http://localhost", "http://localhost:80", "http://localhost:8080", "http://localhost:9090", "http://localhost:3000", "http://localhost:3001","https://sppppq.mysapo.net", "http://192.168.12.61", "http://192.168.12.61:9095", "http://192.168.12.72", "http://192.168.12.72:9095");
        var source = new UrlBasedCorsConfigurationSource();
        var config = new CorsConfiguration();
        config.setAllowedOrigins(origins);
        config.setAllowedMethods(allowed);
        config.setAllowedHeaders(allowed);
        config.setAllowCredentials(true);
        source.registerCorsConfiguration("/api/**", config);
        return new CorsFilter(source);
    }

}
