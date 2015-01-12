package com.erkspace.erkSimpleDAO.observer;

public class ExecutionDateObserver extends Observer{
	
	 public ExecutionDateObserver(ExecutionDate executionDate){
	      this.executionDate = executionDate;
	      this.executionDate.attach(this);
	   }

	   @Override
	   public void update() {		  
	      System.out.println("DB Execution Time is : [" + executionDate.getDate() + "] ");	       
	   }

}
