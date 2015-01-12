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
