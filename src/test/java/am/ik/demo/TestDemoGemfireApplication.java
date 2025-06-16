package am.ik.demo;

import org.springframework.boot.SpringApplication;

public class TestDemoGemfireApplication {

	public static void main(String[] args) {
		SpringApplication.from(DemoGemfireApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
