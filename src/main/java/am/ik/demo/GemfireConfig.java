package am.ik.demo;

import java.util.Properties;
import org.apache.geode.cache.Region;
import org.apache.geode.cache.client.ClientCache;
import org.apache.geode.cache.client.ClientCacheFactory;
import org.apache.geode.cache.client.ClientRegionShortcut;
import org.apache.geode.pdx.ReflectionBasedAutoSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class GemfireConfig {

	@Bean
	ClientCache clientCache(GemfireProps props) {
		Properties properties = new Properties();
		properties.setProperty("log-level", props.logLevel());
		ClientCacheFactory cacheFactory = new ClientCacheFactory(properties);
		for (GemfireProps.Locator locator : props.locators()) {
			cacheFactory.addPoolLocator(locator.host(), locator.port());
		}
		return cacheFactory.setPdxSerializer(new ReflectionBasedAutoSerializer(true, Customer.class.getName()))
			.create();
	}

	@Bean
	Region<Long, Customer> customerRegion(ClientCache clientCache) {
		return clientCache.<Long, Customer>createClientRegionFactory(ClientRegionShortcut.PROXY).create("Customers");
	}

}
