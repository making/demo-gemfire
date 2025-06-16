package am.ik.demo;

import java.util.List;
import org.springframework.data.gemfire.repository.Query;
import org.springframework.data.repository.ListCrudRepository;

public interface CustomerRepository extends ListCrudRepository<Customer, Long> {

	@Query("SELECT * FROM /Customers ORDER BY id")
	List<Customer> findAllOrderById();

}
