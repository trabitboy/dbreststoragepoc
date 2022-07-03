package org.trab.test.dbreststorage;

import java.util.Arrays;

import org.apache.catalina.core.ApplicationContext;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DbreststorageApplication {

	
	
	public static void main(String[] args) {
		SpringApplication.run(DbreststorageApplication.class, args);
	}

	
	//not working
//	 @Bean
//	  public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
//	    return args -> {
//
//	      System.out.println("Let's inspect the beans provided by Spring Boot:");
//
//	      String[] beanNames = ((ListableBeanFactory) ctx).getBeanDefinitionNames();
//	      Arrays.sort(beanNames);
//	      for (String beanName : beanNames) {
//	        System.out.println(beanName);
//	      }
//	    };
//	  }    
	
}
