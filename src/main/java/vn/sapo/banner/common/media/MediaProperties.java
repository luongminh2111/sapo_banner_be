package vn.sapo.banner.common.media;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties(prefix = "provider")
@Getter
@Setter
public class MediaProperties {
    private int timeout;
    private int uploadLimitMb = 2;
    private String media_url;
}