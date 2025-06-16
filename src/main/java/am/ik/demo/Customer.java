package am.ik.demo;

import org.springframework.data.annotation.Id;
import org.springframework.data.gemfire.mapping.annotation.Region;

@Region("Customers")
public record Customer(@Id Long id, String name) {

	public Customer withName(String name) {
		return new Customer(id, name);
	}
}
