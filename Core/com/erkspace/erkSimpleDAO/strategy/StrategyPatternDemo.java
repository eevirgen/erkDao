package com.erkspace.erkSimpleDAO.strategy;

import com.erkspace.erkSimpleDAO.builder.Configuration;
import com.erkspace.erkSimpleDAO.provider.ConnectionProvider;
import com.erkspace.erkSimpleDAO.provider.ConnectionProviderDefaultImplementation;
import com.erkspace.model.Customer;
import com.erkspace.model.Person;

public class StrategyPatternDemo {

	public static void main(String[] args) {
		Configuration configuration = new Configuration.Builder()
								.url("jdbc:mysql://localhost/test")
								.user("root")
								.password("root")
								.build();

		ConnectionProvider provider = new ConnectionProviderDefaultImplementation();

		Context insertContext = new Context(new InsertStrategy(provider, configuration));

		Customer customer = new Customer();
		customer.setName("startegy");
		customer.setId(2000);

		insertContext.execute(customer);

		Person p = new Person();
		p.setName("strategy");
		p.setSurname("startetgyyyyyy");

		insertContext.execute(p);
		
		p.setSurname("updated");
		Context updateContext = new Context(new UpdateStrategy(provider, configuration));
		updateContext.execute(p);
		
		
	}
}
