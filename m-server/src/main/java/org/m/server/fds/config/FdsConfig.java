package org.m.server.fds.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
/*
 * @EnableJpaRepositories( // basePackageClasses = TableADao.class,
 * entityManagerFactoryRef = "fdsEntityManagerFactory", transactionManagerRef
 * ="fdsTransactionManager" )
 */
@EnableTransactionManagement
public class FdsConfig {
	@Bean("fdsDataSource")
	@ConfigurationProperties("app.datasource.fds")
	public DataSource fdsDataSource() {
		System.out.println("fdsDataSource...");
		return DataSourceBuilder.create().build();
	}

	/**
	 * JdbcTemplate 連結
	 * 
	 * @param fdsDataSource
	 * @return
	 */
	@Bean(name = "fdsJdbcTemplate")
	public JdbcTemplate fdsJdbcTemplate(@Qualifier("fdsDataSource") DataSource fdsDataSource) {
		return new JdbcTemplate(fdsDataSource);
	}

	@Bean
	public JdbcTemplate fdsJdbcTemplate() {
		return new JdbcTemplate(fdsDataSource());
	}

	/**
	 * jpa處理方式
	 * 
	 * @param fdsDataSource
	 * @param builder
	 * @return
	 */

	/*
	 * @Bean("fdsEntityManagerFactory") public
	 * LocalContainerEntityManagerFactoryBean fdsEntityManagerFactory(
	 * 
	 * @Qualifier("fdsDataSource") DataSource fdsDataSource,
	 * EntityManagerFactoryBuilder builder) { return builder //
	 * .dataSource(fdsDataSource) // .packages(TableA.class) //
	 * .persistenceUnit("aDs") // .build(); }
	 */

	/**
	 * 手動管理交易
	 * 
	 * @param fdsEntityManagerFactory
	 * @return
	 */
	/*
	 * @Bean("fdsTransactionManager") public PlatformTransactionManager
	 * fdsTransactionManager(
	 * 
	 * @Qualifier("fdsEntityManagerFactory") LocalContainerEntityManagerFactoryBean
	 * fdsEntityManagerFactory) { return new
	 * JpaTransactionManager(fdsEntityManagerFactory.getObject()); }
	 */
}
