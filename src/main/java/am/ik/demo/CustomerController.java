package am.ik.demo;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomerController {

	private final CustomerRepository customerRepository;

	public CustomerController(CustomerRepository customerRepository) {
		this.customerRepository = customerRepository;
	}

	@GetMapping("/customers/{id}")
	ResponseEntity<Customer> getCustomer(@PathVariable Long id) {
		return this.customerRepository.findById(id)
			.map(ResponseEntity::ok)
			.orElseGet(() -> ResponseEntity.notFound().build());
	}

	@GetMapping(path = "/customers")
	List<Customer> getCustomers() {
		return this.customerRepository.findAllOrderById();
	}

	@PostMapping(path = "/customers")
	@ResponseStatus(HttpStatus.CREATED)
	Customer createCustomer(@RequestBody Customer customer) {
		return this.customerRepository.save(customer);
	}

	@PatchMapping(path = "/customers/{id}")
	ResponseEntity<Customer> updateCustomer(@PathVariable Long id, @RequestBody PatchCustomerRequest request) {
		return this.customerRepository.findById(id)
			.map(existingCustomer -> ResponseEntity
				.ok(this.customerRepository.save(existingCustomer.withName(request.name()))))
			.orElseGet(() -> ResponseEntity.notFound().build());
	}

	@DeleteMapping(path = "/customers/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	void deleteCustomer(@PathVariable Long id) {
		this.customerRepository.deleteById(id);
	}

	record PatchCustomerRequest(String name) {
	}

}
