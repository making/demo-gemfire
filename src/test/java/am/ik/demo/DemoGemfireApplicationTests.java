package am.ik.demo;

import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;

import static org.assertj.core.api.Assertions.assertThat;

@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DemoGemfireApplicationTests {

	RestClient restClient;

	@Autowired
	CustomerRepository customerRepository;

	@BeforeEach
	void setup(@LocalServerPort int port, @Autowired RestClient.Builder restClientBuilder) {
		this.restClient = restClientBuilder.baseUrl("http://localhost:" + port)
			.defaultStatusHandler(__ -> true, (req, res) -> {
			})
			.build();
		this.customerRepository.saveAll(List.of(new Customer(1L, "Alice"), new Customer(2L, "Bob"),
				new Customer(3L, "Charlie"), new Customer(4L, "David"), new Customer(5L, "Eve")));
	}

	@AfterEach
	void tearDown() {
		this.customerRepository.deleteAll();
	}

	@Test
	void getAll() {
		ResponseEntity<List<CustomerResponse>> response = this.restClient.get()
			.uri("/customers")
			.retrieve()
			.toEntity(new ParameterizedTypeReference<>() {
			});
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).containsExactly(new CustomerResponse(1, "Alice"), new CustomerResponse(2, "Bob"),
				new CustomerResponse(3, "Charlie"), new CustomerResponse(4, "David"), new CustomerResponse(5, "Eve"));
	}

	@Test
	void getCustomer() {
		ResponseEntity<CustomerResponse> response = this.restClient.get()
			.uri("/customers/1")
			.retrieve()
			.toEntity(CustomerResponse.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).isEqualTo(new CustomerResponse(1, "Alice"));
	}

	@Test
	void getCustomerNotFound() {
		ResponseEntity<String> response = this.restClient.get().uri("/customers/999").retrieve().toEntity(String.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	}

	@Test
	void createCustomer() {
		Customer newCustomer = new Customer(6L, "Frank");
		ResponseEntity<CustomerResponse> response = this.restClient.post()
			.uri("/customers")
			.contentType(MediaType.APPLICATION_JSON)
			.body(newCustomer)
			.retrieve()
			.toEntity(CustomerResponse.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		assertThat(response.getBody()).isEqualTo(new CustomerResponse(6, "Frank"));
	}

	@Test
	void updateCustomer() {
		PatchCustomerRequest request = new PatchCustomerRequest("Alice Updated");
		ResponseEntity<CustomerResponse> response = this.restClient.patch()
			.uri("/customers/1")
			.body(request)
			.retrieve()
			.toEntity(CustomerResponse.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).isEqualTo(new CustomerResponse(1, "Alice Updated"));
	}

	@Test
	void updateCustomerNotFound() {
		PatchCustomerRequest request = new PatchCustomerRequest("Not Found");
		ResponseEntity<String> response = this.restClient.patch()
			.uri("/customers/999")
			.body(request)
			.retrieve()
			.toEntity(String.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	}

	@Test
	void deleteCustomer() {
		ResponseEntity<Void> response = this.restClient.delete().uri("/customers/1").retrieve().toEntity(Void.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
		ResponseEntity<String> getResponse = this.restClient.get()
			.uri("/customers/1")
			.retrieve()
			.toEntity(String.class);
		assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	}

	record CustomerResponse(Integer id, String name) {
	}

	record PatchCustomerRequest(String name) {
	}

}
