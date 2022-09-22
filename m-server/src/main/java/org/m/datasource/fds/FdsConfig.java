package org.m.datasource.fds;

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
//@EnableJpaRepositories(basePackages = "org.must.mapper.fds.entity", entityManagerFactoryRef = "fdsEntityManagerFactory", transactionManagerRef = "fdsDataSourceTransactionManager")
@MapperScan(basePackages = "org.must.mapper.fds.dao", sqlSessionTemplateRef = "fdsSessionTemplate")
public class FdsConfig implements DataSourceImpl {
	@Autowired
	Environment env;

	@Bean("fdsDataSource")
	// @ConfigurationProperties("app.datasource.fds")
	public DataSource fdsDataSource() {
		System.out.println("Create fds DataSource ...");
//		return DataSourceBuilder.create().build();
		return getDataSource(env, "fds");
	}

	/** JdbcTemplate **/
	@Bean(name = "fdsJdbcTemplate")
	public JdbcTemplate fdsJdbcTemplate(@Qualifier("fdsDataSource") DataSource fdsDataSource) {
		return new JdbcTemplate(fdsDataSource);
	}

	public JdbcTemplate fdsTemplate() {
		return new JdbcTemplate(fdsDataSource());
	}

	/** JPA ORM **/
	/*@Bean("fdsEntityManagerFactory")
	public LocalContainerEntityManagerFactoryBean fdsEntityManagerFactory(
			@Qualifier("fdsDataSource") DataSource fdsDataSource, EntityManagerFactoryBuilder builder) {
		return builder.dataSource(fdsDataSource).packages("org.must.mapper.fds.entity") // 設定實體類所在位置
				.persistenceUnit("fdsPersistenceUnit").build();
	}*/

	/** MyBatis ORM **/
	@Bean(name = "fdsSqlSessionFactory")
	public SqlSessionFactory fdsSessionFactory(@Qualifier("fdsDataSource") DataSource dataSource) throws Exception {
		SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
		bean.setDataSource(dataSource);
		bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mapper/fds/*.xml"));
		return bean.getObject();
	}

	@Bean(name = "fdsSessionTemplate")
	public SqlSessionTemplate fdsSessionTemplate(@Qualifier("fdsSqlSessionFactory") SqlSessionFactory sqlSessionFactory)
			throws Exception {
		return new SqlSessionTemplate(sqlSessionFactory);
	}

	/**
	 * 手動管理交易
	 * 
	 * @param fdsEntityManagerFactory
	 * @return
	 */
	@Bean(name = "fdsDataSourceTransactionManager")
	public DataSourceTransactionManager fdsDataSourceTransactionManager(
			@Qualifier("fdsDataSource") DataSource dataSource) {
		return new DataSourceTransactionManager(dataSource);
	}
}
