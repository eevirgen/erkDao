package com.erkspace.erkSimpleDAO.observer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ExecutionDate {
	
   private List<Observer> observers 
      = new ArrayList<Observer>();
   
   private String date;

   public String getDate() {
      return date;
   }

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
}