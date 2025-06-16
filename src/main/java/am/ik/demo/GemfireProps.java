package am.ik.demo;

import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "gemfire")
public record GemfireProps(List<Locator> locators, String logLevel) {

	record Locator(String host, int port) {

		public static Locator valueOf(String hostAndPort) {
			String[] split = hostAndPort.split(":");
			return new Locator(split[0], Integer.parseInt(split[1]));
		}
	}
}
