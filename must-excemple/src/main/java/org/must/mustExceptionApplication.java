package org.must;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan( basePackageClasses = {org.m.datasource.fds.FdsConfig.class, org.m.datasource.oreg.OregConfig.class},basePackages = { "org.must" } )
public class mustExceptionApplication {
	 private static ApplicationContext applicationContext;

	public static void main(String[] args) {

//		applicationContext = new AnnotationConfigApplicationContext(mustToolsApplication.class);
//
//		for (String beanName : applicationContext.getBeanDefinitionNames()) {
//			System.out.println(beanName);
//		}

		SpringApplication.run(mustExceptionApplication.class, args);
	}

	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}

}
