# erkDAO  #
## - Simple Object Persistence Framework - ##

**a draft for training some Design Patterns**

Created a simple Object Persistence Framework called erkDAO. I hope it can give an idea  to someone to create his/her [ORM](http://en.wikipedia.org/wiki/Object-relational_mapping) :) or trying to learn some critical Design Pattern topics. It is simple, useful and only a draft. 

Includes only insert, update and delete operations. And limited with INT and VARCHAR datatypes of mySql.

If you are wavering between the Hibernate and erkDAO, I strongly recommend Hibernate. :P

## Challenges ##
* Factory Pattern,  Proxy Pattern, Observer Pattern, Builder Pattern, Provider Pattern and Strategy Pattern are **MUST**
* Code must be extendable without modification (Open Close Principle)

### Factory Pattern ###

In object-oriented programming, a factory is an object for creating other objects – formally a factory is simply an object that returns an object from some method call, which is assumed to be "new"

We have a DAOFactory which includes a Map which holds DAO instance which has been provided.
Basically it creates our myDAO Object

```
#!java
	@SuppressWarnings("unchecked")
	public <T> T getDAO(Class<T> klass) {
		if (!klass.isInterface()) {
			throw new RuntimeException("Class: " + klass.getName() + " must be interface");
		}
		if (factories.containsKey(klass)) {
			return (T) factories.get(klass);
		}
		Object proxy = createDAO(klass);
		factories.put(klass, proxy);
		return (T) proxy;
	}

```

### Proxy Pattern ###
A proxy, in its most general form, is a class functioning as an interface to something else. The proxy could interface to anything: a network connection, a large object in memory, a file, or some other resource that is expensive or impossible to duplicate.

The proxy design pattern allows you to provide an interface to other objects by creating a wrapper class as the proxy. The wrapper class, which is the proxy, can add additional functionality to the object of interest without changing the object's code. Below are some of the common examples in which the proxy pattern is used,

I used  java.lang.reflect.Proxy.newProxyInstance to achieve that :

```
#!java

       protected Object createDAO(Class<?> klass) {
		// Proxy instance has been created. To interception 
		DAOProxy proxy = new DAOProxy(connectionProvider, configuration);
		ClassLoader cl = getClass().getClassLoader();
		Object dao = newProxyInstance(cl, new Class[] { klass }, proxy);
		return dao;
	}
```

**DAOProxy** class is so important. It is used by JVM :

```
#!java
        // We intercept when MYDao interface method calls. these methods runs JVM.
	// We have to know which execution we have to execute. So we get method name
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		String name = method.getName();
		Object model = args[0];
		if (INSERT.equals(name)) {
			String query = new InsertBuilder().build(model);
			insert(model, query);
		} else if (DELETE.equals(name)) {
			String criteria = (String) args[1];
			Class<?> klass = (Class<?>) args[0];
			String query = new DeleteBuilder().build(klass, criteria);
			delete(query);
		} else if (UPDATE.equals(name)) {
			String criteria = (String) args[1];
			String query = new UpdateBuilder().build(model);
			update(model, query + " WHERE " + criteria);
		}
		return null;
	}
```
### Observer Pattern ###

The observer pattern is a software design pattern in which an object, called the subject, maintains a list of its dependents, called observers, and notifies them automatically of any state changes, usually by calling one of their methods. It is mainly used to implement distributed event handling systems.

I created an ExecutionDate POJO. And when date propery change, it notifies All Observers. 


```
#!java

   public void setDate(java.util.Date date) {
	   this.date = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSS").format(date);
	   notifyAllObservers();
   }

   public void attach(Observer observer){
      observers.add(observer);		
   }

   public void notifyAllObservers(){
      for (Observer observer : observers) {
         observer.update();
      }
   } 	
```

And in QueryHandler (which is executes created queries) I changed the execution date after every execution : 

In QueryHandler construction:

```
#!java
new ExecutionDateObserver(executionDate);
```

and after statement.
```
#!java
pstmt.executeUpdate();
executionDate.setDate(new Date());

```

Remember it is only about to apply Observer Pattern. I have no any other point to do that.
And also remember Observer is an abstract class.

### Builder Pattern ###

The intent of the Builder design pattern is to separate the construction of a complex object from its representation.

Please note our DB Connection provider needs aconfiguration which is build in constructor. And it has no setters :

```
#!java

package com.erkspace.erkSimpleDAO.builder;

public class Configuration {

	private String user;

	private String password;

	private String url;

	public static class Builder {

		private Configuration configuration = new Configuration();

		public Builder url(String url) {
			this.configuration.url = url;
			return this;
		}

		public Builder user(String user) {
			this.configuration.user = user;
			return this;
		}

		public Builder password(String password) {
			this.configuration.password = password;
			return this;
		}

		public Configuration build() {
			return configuration;
		}
	}

	public String getUser() {
		return user;
	}

	public String getPassword() {
		return password;
	}

	public String getUrl() {
		return url;
	}

	@Override
	public String toString() {
		return "Configuration [user=" + user + ", password=" + password
				+ ", url=" + url + "]";
	}
}

```
### Provider Pattern ###

It is used to allow an application to choose from one of multiple implementations or "condiments" in the application configuration, for example, to provide access to different data stores to retrieve login information, or to use different storage methodologies such as a database, binary to disk, XML, etc.

We have ConnectionProvider which is an interface and its implementation ConnectionProviderDefaultImplementation. (Please don't get a hang-up about naming conventions)


```
#!java
package com.erkspace.erkSimpleDAO.provider;

import java.sql.Connection;
import java.sql.SQLException;

import com.erkspace.erkSimpleDAO.builder.Configuration;

public interface ConnectionProvider {

	public Connection getConnection(Configuration configuration) throws SQLException;
}
```

and 


```
#!java


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.erkspace.erkSimpleDAO.builder.Configuration;

public class ConnectionProviderDefaultImplementation implements ConnectionProvider {

	@Override
	public Connection getConnection(Configuration configuration)
			throws SQLException {
		return DriverManager.getConnection(configuration.getUrl(), configuration.getUser(), configuration.getPassword());
	}
}

```

and last one 

### Strategy Pattern ###
The strategy pattern (also known as the policy pattern) is a software design pattern that enables an algorithm's behavior to be selected at runtime. For instance, a class that performs validation on incoming data may use a strategy pattern to select a validation algorithm based on the type of data, the source of the data, user choice, or other discriminating factors. These factors are not known for each case until run-time, and may require radically different validation to be performed. The validation strategies, encapsulated separately from the validating object, may be used by other validating objects in different areas of the system (or even different systems) without code duplication.

We have an interface :


```
#!java

package com.erkspace.erkSimpleDAO.strategy;

public interface Strategy {

	void execute(Object model);
}


```

And Context :

```
#!java
package com.erkspace.erkSimpleDAO.strategy;

public class Context {

	private Strategy strategy;

	public Context(Strategy strategy) {
		this.strategy = strategy;
	}

	public void execute(Object model) {
		strategy.execute(model);
	}
}

```

And our Insert and Update strategies :
(Please note they extends QueryHandler and implements Strategy )


```
#!java
package com.erkspace.erkSimpleDAO.strategy;

import com.erkspace.erkSimpleDAO.builder.Configuration;
import com.erkspace.erkSimpleDAO.provider.ConnectionProvider;
import com.erkspace.erkSimpleDAO.utils.InsertBuilder;
import com.erkspace.erkSimpleDAO.utils.QueryHandler;

public class InsertStrategy extends QueryHandler implements Strategy {

	public InsertStrategy(ConnectionProvider provider, Configuration configuration) {
		super(provider, configuration);
	}

	@Override
	public void execute(Object model) {
		InsertBuilder builder = new InsertBuilder();
		String query = builder.build(model);
		try {
			insert(model, query);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}

package com.erkspace.erkSimpleDAO.strategy;

import com.erkspace.erkSimpleDAO.builder.Configuration;
import com.erkspace.erkSimpleDAO.provider.ConnectionProvider;
import com.erkspace.erkSimpleDAO.utils.QueryHandler;
import com.erkspace.erkSimpleDAO.utils.UpdateBuilder;

public class UpdateStrategy extends QueryHandler implements Strategy {

	public UpdateStrategy(ConnectionProvider provider, Configuration configuration) {
		super(provider, configuration);
	}

	@Override
	public void execute(Object model) {
		UpdateBuilder builder = new UpdateBuilder();
		String query;
		try {
			query = builder.build(model);
		} catch (IllegalArgumentException | IllegalAccessException ex) {
			throw new RuntimeException(ex);
		}
		try {
			update(model, query);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}

```

And of course we have a demo to show it! 


```
#!java

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

```

the project is also close to the Open Close principles. You can easily add a new Model which refers to a mySql table, And add a new insert/update/delete method on myDAO interface which pass the Model. And you can use it. And connection provider can be also extendable. 

In OCP point of view QueryHandler needs some modifications.


Enjoy!,
erk