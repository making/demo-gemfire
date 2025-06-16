package am.ik.demo;

import java.util.Objects;
import org.apache.geode.pdx.PdxReader;
import org.apache.geode.pdx.PdxSerializable;
import org.apache.geode.pdx.PdxWriter;

public class Customer implements PdxSerializable {

	private Long id;

	private String name;

	public Customer(Long id, String name) {
		this.id = id;
		this.name = name;
	}

	public Customer() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Customer withName(String name) {
		return new Customer(id, name);
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Customer customer))
			return false;
		return Objects.equals(id, customer.id) && Objects.equals(name, customer.name);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, name);
	}

	@Override
	public String toString() {
		return "Customer{" + "id=" + id + ", name='" + name + '\'' + '}';
	}

	@Override
	public void toData(PdxWriter pdxWriter) {
		pdxWriter.writeLong("id", this.getId());
		pdxWriter.writeString("name", this.getName());
	}

	@Override
	public void fromData(PdxReader pdxReader) {
		this.setId(pdxReader.readLong("id"));
		this.setName(pdxReader.readString("name"));
	}

}
