package org.must;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = { "org.m.server.fds", "org.must" })
public class mustToolsApplication {
	 private static ApplicationContext applicationContext;

	public static void main(String[] args) {

//		applicationContext = new AnnotationConfigApplicationContext(mustToolsApplication.class);
//
//		for (String beanName : applicationContext.getBeanDefinitionNames()) {
//			System.out.println(beanName);
//		}

		SpringApplication.run(mustToolsApplication.class, args);
	}

	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}

}
