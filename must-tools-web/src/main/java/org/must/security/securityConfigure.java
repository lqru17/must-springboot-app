/*******************************************************************************
 * Project Key : MÜST869759 Create on 2021年4月26日 下午3:30:03 Copyright (c) 2021.中華音樂著作權協會版權所有.
 * 注意：本內容僅限於中華音樂著作權協會內部傳閱，禁止外洩以及用於其他商業目的
 ******************************************************************************/

package org.must.security;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * <P>TODO</P>
 * 
 * @version $Id$
 * @user xxx 2021年8月10日 下午3:30:03
 */
@EnableWebSecurity
public class securityConfigure extends WebSecurityConfigurerAdapter {
	
	
	
	/*
	 * @Bean public UserDetailsService userDetailsService() {
	 * System.out.println("進入login userDetailsService page"); return new LoginService(); }
	 */
     
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
     
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
         
        return authProvider;
    }
 
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }
	 
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// TODO
		System.out.println("------security authorize------");

		
		  //http 
		  //.authorizeRequests() .antMatchers(HttpMethod.GET,
		  //"/onlinemembermeeting/user123/**").authenticated()
		  //.antMatchers(HttpMethod.GET).permitAll() 
		  //.antMatchers(HttpMethod.POST).permitAll() 
		  //.anyRequest().authenticated() 
		  //.and() 		 
		  //.csrf().disable() //close CSRF防護
		  //.formLogin().loginPage("/onlinemembermeeting/login");
		 
		  http.csrf().disable(); //close CSRF防護
	}

}
