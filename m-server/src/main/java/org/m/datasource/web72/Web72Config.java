package org.m.datasource.web72;

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
//@EnableJpaRepositories(basePackages = "org.must.mapper.web72.entity", entityManagerFactoryRef = "web72EntityManagerFactory", transactionManagerRef = "web72DataSourceTransactionManager")
@MapperScan(basePackages = "org.must.mapper.web72.dao", sqlSessionTemplateRef = "web72SessionTemplate")
public class Web72Config implements DataSourceImpl {
	@Autowired
	Environment env;

	@Bean("web72DataSource")
	public DataSource web72DataSource() {
		System.out.println("web72DataSource...");
		return getDataSource(env, "web72");
	}

	/** JdbcTemplate **/
	@Bean(name = "web72JdbcTemplate")
	public JdbcTemplate web72JdbcTemplate(@Qualifier("web72DataSource") DataSource web72DataSource) {
		return new JdbcTemplate(web72DataSource);
	}

	public JdbcTemplate web72Template() {
		return new JdbcTemplate(web72DataSource());
	}

	/** JPA ORM **/
	/*@Bean("web72EntityManagerFactory")
	public LocalContainerEntityManagerFactoryBean web72EntityManagerFactory(
			@Qualifier("web72DataSource") DataSource web72DataSource, EntityManagerFactoryBuilder builder) {
		return builder.dataSource(web72DataSource).packages("org.m.server.web72.entity") // 設定實體類所在位置
				.persistenceUnit("web72PersistenceUnit").build();
	}*/

	/** MyBatis ORM **/
	@Bean(name = "web72SqlSessionFactory")
	public SqlSessionFactory web72SessionFactory(@Qualifier("web72DataSource") DataSource dataSource) throws Exception {
		SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
		bean.setDataSource(dataSource);
		bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mapper/web72/*.xml"));
		return bean.getObject();
	}

	@Bean(name = "web72SessionTemplate")
	public SqlSessionTemplate web72SessionTemplate(@Qualifier("web72SqlSessionFactory") SqlSessionFactory sqlSessionFactory)
			throws Exception {
		return new SqlSessionTemplate(sqlSessionFactory);
	}

	/**
	 * 手動管理交易
	 * 
	 * @param web72EntityManagerFactory
	 * @return
	 */
	@Bean(name = "web72DataSourceTransactionManager")
	public DataSourceTransactionManager web72DataSourceTransactionManager(
			@Qualifier("web72DataSource") DataSource dataSource) {
		return new DataSourceTransactionManager(dataSource);
	}
}
