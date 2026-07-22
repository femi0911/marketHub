package africa.estore.markethub.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "paystack.payment")
@Getter
@Setter
public class PayStackConfig {
    private String url;
    private String key;
}
