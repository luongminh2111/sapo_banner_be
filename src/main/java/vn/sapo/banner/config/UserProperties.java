package vn.sapo.banner.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "sapo.security", ignoreInvalidFields = true)
public class UserProperties {
    Map<String, List<String>> users = new HashMap<>();
}
