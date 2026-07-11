package africa.estore.markethub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
@EnableConfigurationProperties
@ConfigurationPropertiesScan("africa.estore.markethub.config")
public class MarkethubApplication {

	public static void main(String[] args) {
		SpringApplication.run(MarkethubApplication.class, args);
	}

}
