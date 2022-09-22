package org.m.datasource.oreg;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.m.datasource.impl.DataSourceImpl;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

@Configuration
//@EnableJpaRepositories(basePackages = "org.must.mapper.oreg.entity", entityManagerFactoryRef = "oregEntityManagerFactory", transactionManagerRef = "oregDataSourceTransactionManager")
@MapperScan(basePackages = "org.must.mapper.oreg.dao", sqlSessionTemplateRef = "oregSessionTemplate")
public class OregConfig implements DataSourceImpl{
	@Autowired Environment env;
	
	@Bean("oregDataSource")
	public DataSource oregDataSource() {
		System.out.println("Create oreg DataSource ...");
		return getDataSource(env, "oreg");
	}

	/** JdbcTemplate **/
	@Bean(name = "oregJdbcTemplate")
	public JdbcTemplate oregJdbcTemplate(@Qualifier("oregDataSource") DataSource oregDataSource) {
		return new JdbcTemplate(oregDataSource);
	}

	public JdbcTemplate oregTemplate() {
		return new JdbcTemplate(oregDataSource());
	}

	/** JPA ORM **/
	/*@Bean("oregEntityManagerFactory")
	public LocalContainerEntityManagerFactoryBean oregEntityManagerFactory(
			@Qualifier("oregDataSource") DataSource oregDataSource, EntityManagerFactoryBuilder builder) {
		return builder.dataSource(oregDataSource).packages("org.m.server.oreg.entity") // 設定實體類所在位置
				.persistenceUnit("oregPersistenceUnit").build();
	}*/

	/** MyBatis ORM **/
	@Bean(name = "oregSqlSessionFactory")
	public SqlSessionFactory oregSessionFactory(@Qualifier("oregDataSource") DataSource dataSource) throws Exception {
		SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
		bean.setDataSource(dataSource);
		bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mapper/oreg/*.xml"));
		return bean.getObject();
	}

	@Bean(name = "oregSessionTemplate")
	public SqlSessionTemplate oregSessionTemplate(@Qualifier("oregSqlSessionFactory") SqlSessionFactory sqlSessionFactory)
			throws Exception {
		return new SqlSessionTemplate(sqlSessionFactory);
	}

	/**
	 * 手動管理交易
	 * 
	 * @param oregEntityManagerFactory
	 * @return
	 */
	@Bean(name = "oregDataSourceTransactionManager")
	public DataSourceTransactionManager oregDataSourceTransactionManager(
			@Qualifier("oregDataSource") DataSource dataSource) {
		return new DataSourceTransactionManager(dataSource);
	}
}
