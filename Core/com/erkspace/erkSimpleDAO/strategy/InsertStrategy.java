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
