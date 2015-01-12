package com.erkspace.test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.erkspace.erkSimpleDAO.builder.Configuration;
import com.erkspace.erkSimpleDAO.factory.DAOFactory;
import com.erkspace.erkSimpleDAO.provider.ConnectionProvider;
import com.erkspace.erkSimpleDAO.provider.ConnectionProviderDefaultImplementation;
import com.erkspace.model.Customer;
import com.erkspace.model.Person;
import com.erkspace.test.dao.MyDAO;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DAOTest {

	private Configuration configuration;

	private MyDAO dao;

	@Before
	public void initTest() {
		configuration = new Configuration.Builder()
										.url("jdbc:mysql://localhost/test")
										.user("root")
										.password("root")
										.build();

		ConnectionProvider provider = new ConnectionProviderDefaultImplementation();
		dao = new DAOFactory(provider, configuration).getDAO(MyDAO.class);
	}
	protected Connection getConnection() throws SQLException {
		ConnectionProvider provider = new ConnectionProviderDefaultImplementation();
		return provider.getConnection(configuration);
	}

	@Test
	public void t01_insert() throws SQLException {
		dao.insert(new Person("foo", "bar"));
		
		try (Connection conn = getConnection()) {
			ResultSet rs = conn.createStatement().executeQuery("select * from PERSON where NAME = 'foo'");
			rs.next();
			String surname = rs.getString("SURNAME");
			Assert.assertEquals("bar", surname);
		}
	}

	@Test
	public void t02_delete() throws SQLException {
		dao.delete(Person.class, " NAME = 'foo'");

		try (Connection conn = getConnection()) {
			ResultSet rs = conn.createStatement().executeQuery("select * from PERSON where NAME = 'foo'");
			Assert.assertFalse(rs.next());
		}
	}

	@Test
	public void t03_update() throws SQLException {
		t01_insert();

		Person p = new Person();
		p.setName("xyz");
		dao.update(p, "NAME = 'foo'");

		try (Connection conn = getConnection()) {
			ResultSet rs = conn.createStatement().executeQuery("select * from PERSON where SURNAME = 'bar'");
			rs.next();
			String surname = rs.getString("NAME");
			Assert.assertEquals("xyz", surname);
		}
	}
	
	@Test
	public void t04_insert_customer() {
		Customer c = new Customer();
		c.setId(20);
		c.setName("test");
		dao.insert(c);
	}
}

