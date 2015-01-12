package com.erkspace.test.dao;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Random;

public class SampleProxy {

	public static interface MessageService {
		
		String sayHello(String message);
	}
	
	public static interface LanguageService {
		
		String translate(String message);
	}
	
	public static class Translator implements MessageService, LanguageService {

		@Override
		public String sayHello(String message) {
			try {
				Thread.sleep(100 * new Random().nextInt(10));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return "hello, " + message;
		}

		@Override
		public String translate(String message) {
			if ("hello".equals(message)) {
				return "merhaba";
			}
			return "hello";
		}
	}

	public static class LoggingHandler implements InvocationHandler {

		private Object target;
		
		public LoggingHandler(Object target) {
			this.target = target;
		}

		@Override
		public Object invoke(Object proxy, Method method, Object[] args)
				throws Throwable {
			long startTime = System.currentTimeMillis();
			
			Object retValue = method.invoke(target, args);

			long endTime = System.currentTimeMillis();
			
			System.out.println("Elapsed Time: " + (endTime - startTime));
			
			return retValue;
		}
	}

	public static void main(String[] args) {
		Object proxyInstance = Proxy.newProxyInstance(SampleProxy.class.getClassLoader(), new Class[] { MessageService.class, LanguageService.class }, new LoggingHandler(new Translator()));
		
		MessageService msgService = (MessageService) proxyInstance;
		LanguageService langService = (LanguageService) proxyInstance;
		System.out.println(msgService.sayHello(langService.translate("hello")));
	}

}
