package org.m.datasource.impl;

import javax.sql.DataSource;

import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

public interface DataSourceImpl {
	public default DataSource getDataSource(Environment env, String envProperty) {
	    final String datasourceUrl = env.getRequiredProperty("app.datasource."+envProperty+".jdbc-url");
	    //System.out.println("url:"+datasourceUrl);
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
	    dataSource.setDriverClassName(env.getProperty("app.datasource."+envProperty+".driver-class-name"));
	    dataSource.setUrl(env.getProperty("app.datasource."+envProperty+".jdbc-url"));
	    dataSource.setUsername(env.getProperty("app.datasource."+envProperty+".username"));
	    dataSource.setPassword(env.getProperty("app.datasource."+envProperty+".password"));

	    return dataSource;
	}
}
