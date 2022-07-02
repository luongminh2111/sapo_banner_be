package vn.sapo.banner.common.media;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Import(MediaProviderConfiguration.class)
@EnableConfigurationProperties({AwsProperties.class, MediaProperties.class})
public @interface EnableMediaProvider {
}
