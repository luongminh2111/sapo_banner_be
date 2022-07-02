package vn.sapo.banner.common.media;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties(prefix="aws")
@Getter
@Setter
public class AwsProperties {
	private String bucket_name;
	private String access_key;
	private String secret_key;
	private String media_url;
}