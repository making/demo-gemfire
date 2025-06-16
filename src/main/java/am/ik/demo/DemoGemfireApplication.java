package am.ik.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class DemoGemfireApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoGemfireApplication.class, args);
	}

}
