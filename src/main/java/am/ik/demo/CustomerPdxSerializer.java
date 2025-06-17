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
		customer.toData(pdxWriter);
		return true;
	}

	@Override
	public Object fromData(Class<?> clazz, PdxReader pdxReader) {
		if (!clazz.equals(Customer.class)) {
			return null;
		}
		Customer customer = new Customer();
		customer.fromData(pdxReader);
		return customer;
	}

}
