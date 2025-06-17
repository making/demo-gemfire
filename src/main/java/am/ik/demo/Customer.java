package am.ik.demo;

public record Customer(Long id, String name) {

	public Customer withName(String name) {
		return new Customer(this.id, name);
	}
}
