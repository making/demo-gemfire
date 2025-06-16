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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api")
public class CustomerController {

	private final CustomerRepository customerRepository;

	public CustomerController(CustomerRepository customerRepository) {
		this.customerRepository = customerRepository;
	}

	@GetMapping("/customers/{id}")
	ResponseEntity<CustomerResponse> getCustomer(@PathVariable Long id) {
		return this.customerRepository.findById(id)
			.map(CustomerResponse::from)
			.map(ResponseEntity::ok)
			.orElseGet(() -> ResponseEntity.notFound().build());
	}

	@GetMapping(path = "/customers")
	List<CustomerResponse> getCustomers() {
		return this.customerRepository.findAllOrderById().stream().map(CustomerResponse::from).toList();
	}

	@PostMapping(path = "/customers")
	@ResponseStatus(HttpStatus.CREATED)
	CustomerResponse createCustomer(@RequestBody Customer customer) {
		Customer saved = this.customerRepository.save(customer);
		return CustomerResponse.from(saved);
	}

	@PatchMapping(path = "/customers/{id}")
	ResponseEntity<CustomerResponse> updateCustomer(@PathVariable Long id, @RequestBody PatchCustomerRequest request) {
		return this.customerRepository.findById(id).map(existingCustomer -> {
			Customer saved = this.customerRepository.save(existingCustomer.withName(request.name()));
			return ResponseEntity.ok(CustomerResponse.from(saved));
		}).orElseGet(() -> ResponseEntity.notFound().build());
	}

	@DeleteMapping(path = "/customers/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	void deleteCustomer(@PathVariable Long id) {
		this.customerRepository.deleteById(id);
	}

	record CustomerResponse(Long id, String name) {

		static CustomerResponse from(Customer customer) {
			return new CustomerResponse(customer.getId(), customer.getName());
		}
	}

	record PatchCustomerRequest(String name) {
	}

}
