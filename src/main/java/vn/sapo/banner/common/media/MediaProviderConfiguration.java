package vn.sapo.banner.common.media;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@ConditionalOnBean({AwsProperties.class, MediaProperties.class})
@Configuration
public class MediaProviderConfiguration {

    @Bean
    public MediaProvider mediaProvider(MediaProperties mediaConfig, AwsProperties awsConfig, @Value("${spring.profiles:local}") String profile) {
        return new SapoMediaProvider(mediaConfig, awsConfig, profile);
    }

}
