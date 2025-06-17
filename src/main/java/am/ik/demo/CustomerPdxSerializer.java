package am.ik.demo;

import org.apache.geode.cache.Declarable;
import org.apache.geode.pdx.PdxReader;
import org.apache.geode.pdx.PdxSerializer;
import org.apache.geode.pdx.PdxWriter;

public class CustomerPdxSerializer implements PdxSerializer, Declarable {

	@Override
	public boolean toData(Object o, PdxWriter pdxWriter) {
		if (!(o instanceof Customer customer)) {
			return false;
		}
		pdxWriter.writeLong("id", customer.id());
		pdxWriter.writeString("name", customer.name());
		return true;
	}

	@Override
	public Object fromData(Class<?> clazz, PdxReader pdxReader) {
		if (!clazz.equals(Customer.class)) {
			return null;
		}
		return new Customer(pdxReader.readLong("id"), pdxReader.readString("name"));
	}

}
