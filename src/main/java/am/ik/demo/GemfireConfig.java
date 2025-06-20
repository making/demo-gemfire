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
		if (props.properties() != null) {
			properties.putAll(props.properties());
		}
		ClientCacheFactory cacheFactory = new ClientCacheFactory(properties)
			.setPdxSerializer(new ReflectionBasedAutoSerializer(Customer.class.getName()));
		for (var locator : props.locators()) {
			cacheFactory.addPoolLocator(locator.host(), locator.port());
		}
		return cacheFactory.create();
	}

	@Bean
	Region<Long, Customer> customerRegion(ClientCache clientCache) {
		return clientCache.<Long, Customer>createClientRegionFactory(ClientRegionShortcut.PROXY).create("Customers");
	}

}
