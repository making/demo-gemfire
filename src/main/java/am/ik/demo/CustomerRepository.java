package am.ik.demo;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.apache.geode.cache.Region;
import org.apache.geode.cache.client.ClientCache;
import org.apache.geode.cache.query.FunctionDomainException;
import org.apache.geode.cache.query.NameResolutionException;
import org.apache.geode.cache.query.QueryInvocationTargetException;
import org.apache.geode.cache.query.QueryService;
import org.apache.geode.cache.query.SelectResults;
import org.apache.geode.cache.query.TypeMismatchException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

@Repository
public class CustomerRepository {

	private final Region<Long, Customer> region;

	private final QueryService queryService;

	public CustomerRepository(@Qualifier("customerRegion") Region<Long, Customer> region, ClientCache clientCache) {
		this.region = region;
		this.queryService = clientCache.getQueryService();
	}

	public Optional<Customer> findById(Long id) {
		return Optional.ofNullable(this.region.get(id));
	};

	@SuppressWarnings("unchecked")
	public List<Customer> findAllOrderById() {
		try {
			return ((SelectResults<Customer>) this.queryService.newQuery("SELECT * FROM /Customers ORDER BY id")
				.execute()).asList();
		}
		catch (FunctionDomainException | QueryInvocationTargetException | NameResolutionException
				| TypeMismatchException e) {
			throw new RuntimeException(e);
		}
	}

	public Customer save(Customer customer) {
		this.region.put(customer.getId(), customer);
		return customer;
	}

	public void deleteById(Long id) {
		this.region.remove(id);
	}

	public void saveAll(List<Customer> customers) {
		this.region.putAll(customers.stream().collect(Collectors.toMap(Customer::getId, Function.identity())));
	}

	public void deleteAll() {
		this.region.removeAll(this.region.keySet());
	}

}
