package com.erkspace.test.dao;

import com.erkspace.model.Customer;
import com.erkspace.model.Person;

public interface MyDAO {

	void insert(Person p);
	
	void insert(Customer customer);
	
	void delete(Class<?> klass, String criteria);

	void update(Person p, String criteria);
}
