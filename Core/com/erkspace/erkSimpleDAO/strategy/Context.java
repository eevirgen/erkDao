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
