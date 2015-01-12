package com.erkspace.test;

import org.junit.Assert;
import org.junit.Test;

import com.erkspace.erkSimpleDAO.utils.DeleteBuilder;
import com.erkspace.erkSimpleDAO.utils.InsertBuilder;
import com.erkspace.erkSimpleDAO.utils.UpdateBuilder;
import com.erkspace.model.Person;

public class BuilderTest {

	@Test
	public void testInsert() {
		InsertBuilder insert = new InsertBuilder();
		String query = insert.build(new Person());
		Assert.assertEquals("INSERT INTO PERSON (NAME, SURNAME) VALUES (?, ?)", query);
	}

	@Test
	public void testDelete() {
		DeleteBuilder builder = new DeleteBuilder();
		String query = builder.build(Person.class, "NAME = 'foo'");
		Assert.assertEquals("DELETE FROM Person WHERE NAME = 'foo'", query);
	}

	@Test
	public void testUpdate() throws IllegalArgumentException, IllegalAccessException {
		UpdateBuilder builder = new UpdateBuilder();
		Person p = new Person();
		p.setName("foo");
		p.setSurname("bar");
		String query = builder.build(p);
		Assert.assertEquals("UPDATE PERSON SET NAME = ?, SURNAME = ?", query);
	}
}
