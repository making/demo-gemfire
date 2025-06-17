package am.ik.demo;

import org.apache.geode.cache.Region;
import org.apache.geode.cache.client.ClientCache;
import org.apache.geode.cache.client.ClientCacheFactory;
import org.apache.geode.cache.client.ClientRegionShortcut;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class GemfireConfig {

	@Bean
	ClientCache clientCache(GemfireProps props) {
		ClientCacheFactory cacheFactory = new ClientCacheFactory().set("log-level", props.logLevel());
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
