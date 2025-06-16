package am.ik.demo;

import com.vmware.gemfire.testcontainers.GemFireCluster;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.DynamicPropertyRegistrar;

@TestConfiguration(proxyBeanMethods = false)
class TestcontainersConfiguration {

	@Bean
	GemFireCluster cluster() {
		GemFireCluster cluster = new GemFireCluster(1, 1);
		cluster.acceptLicense().start();
		cluster.gfsh(false, "create region --name=Customers --type=PARTITION");
		return cluster;
	}

	@Bean
	DynamicPropertyRegistrar dynamicPropertyRegistrar(GemFireCluster cluster) {
		return registry -> registry.add("gemfire.locators", () -> "127.0.0.1:%d".formatted(cluster.getLocatorPort()));
	}

}
