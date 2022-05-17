package com.basic.operations.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.stereotype.Component;

import com.basic.operations.models.User;
import com.basic.operations.services.impl.UserServices;

@Component
@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter{

	@Autowired
	UserServices userServies;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    	auth.userDetailsService(userServies).passwordEncoder(User.PASSWORD_ENCODER);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
		.antMatchers("/api/signUpUser","/api/socialLogin", "/api/loginApp", "/api/loginOwner", "/api/addTempData", "/api/forgetPassword" , "{id}/getStreetRulesList/page/{page}","/admin/checkAuthentication","/admin/logoutAdmin","/admin/resetPassword","/owner/getStripeUserCode","/api/addTimeZoneForStreets").permitAll()
		.antMatchers("/api/**").authenticated()
		.antMatchers("/api/**").hasAnyRole("USER")
		
		.antMatchers("/admin/checkAuthentication", "/admin/mobileTermsAndConditionsPage", "/admin/termsandconditions").permitAll()
		.antMatchers("/admin/**").authenticated()
		.antMatchers("/admin/**").hasRole("ADMIN")
		.and()
        .httpBasic()
        .and()
        .csrf().disable().cors();
    }
    
}
