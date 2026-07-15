package africa.estore.markethub.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "cloudinary.api")
@Getter
@Setter
public class CloudinaryConfig {
    private String key;
    private String secret;
    private String name;
}
