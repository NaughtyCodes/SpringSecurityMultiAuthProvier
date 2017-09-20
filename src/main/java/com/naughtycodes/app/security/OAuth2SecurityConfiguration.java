package com.naughtycodes.app.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.approval.TokenApprovalStore;
import org.springframework.security.oauth2.provider.approval.TokenStoreUserApprovalHandler;
import org.springframework.security.oauth2.provider.request.DefaultOAuth2RequestFactory;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.naughtycodes.app.configuration.SpringFilter;
import com.naughtycodes.app.service.impl.CustomUserDetailsService;

@Configuration
@EnableWebSecurity
//@Order(2)
public class OAuth2SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	private ClientDetailsService clientDetailsService;
	
	@Autowired
	private CustomUserDetailsService customUserDetailsService;
	
	@Autowired
    public void globalUserDetails(AuthenticationManagerBuilder auth) throws Exception {
		System.out.println("call-->> globalUserDetails");
//		customUserDetailsService = new CustomUserDetailsService();
		auth.inMemoryAuthentication().withUser("admin").password("pwd").roles("ADMIN");
		auth.userDetailsService(customUserDetailsService);
		
    }

	
//-----------------------------------------------------------------------------	
	@Bean
	@Override
	protected AuthenticationManager authenticationManager() throws Exception {
		final List<AuthenticationProvider> providers = new ArrayList<>(1);
		providers.add(preauthAuthProvider());
		return new ProviderManager(providers);
	}
	
	@Bean(name = "siteminderFilter")
	public RequestHeaderAuthenticationFilter siteminderFilter() throws Exception {
		RequestHeaderAuthenticationFilter requestHeaderAuthenticationFilter = new RequestHeaderAuthenticationFilter();
		requestHeaderAuthenticationFilter.setPrincipalRequestHeader("DBA_USER");
		requestHeaderAuthenticationFilter.setAuthenticationManager(authenticationManager());
		return requestHeaderAuthenticationFilter;
	}
 
	@Bean(name = "preAuthProvider")
	PreAuthenticatedAuthenticationProvider preauthAuthProvider() throws Exception {
		PreAuthenticatedAuthenticationProvider provider = new PreAuthenticatedAuthenticationProvider();
		provider.setPreAuthenticatedUserDetailsService(userDetailsServiceWrapper());
		return provider;
	}
	
	@Bean
	UserDetailsByNameServiceWrapper<PreAuthenticatedAuthenticationToken> userDetailsServiceWrapper() throws Exception {
		UserDetailsByNameServiceWrapper<PreAuthenticatedAuthenticationToken> wrapper = new UserDetailsByNameServiceWrapper<>();
		wrapper.setUserDetailsService(customUserDetailsService);
		return wrapper;
	}

//-----------------------------------------------------------------------------
	
    @Override
    protected void configure(HttpSecurity http) throws Exception {

//      http.addFilterBefore(new SpringFilter(), BasicAuthenticationFilter.class);
//      http.authorizeRequests()
//        	.antMatchers("/log").hasAuthority("ROLE_ADMIN")
//    	.and().authorizeRequests()
//    		.anyRequest().denyAll();

  	  http
		.csrf().disable()
		.anonymous().disable()
	  	.authorizeRequests()
	  	.antMatchers("/log").hasAuthority("ADMIN")
	  	.antMatchers("/oauth/token").permitAll();
	    http.addFilterAfter(siteminderFilter(), RequestHeaderAuthenticationFilter.class).authorizeRequests().antMatchers("/").permitAll().anyRequest().authenticated().antMatchers("/log").hasAuthority("ROLE_ADMIN");
		
//  	  http.authorizeRequests()
//		  .antMatchers("/", "/home").permitAll()
//		  .antMatchers("/admin/**").access("hasRole('ADMIN')")
//		  .antMatchers("/db/**").access("hasRole('ADMIN') and hasRole('DBA')")
//		  .and().formLogin().loginPage("/login")
//		  .usernameParameter("ssoId").passwordParameter("password")
//		  .and().exceptionHandling().accessDeniedPage("/Access_Denied");

    }
    

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }


	@Bean
	public TokenStore tokenStore() {
		return new InMemoryTokenStore();
	}

	@Bean
	@Autowired
	public TokenStoreUserApprovalHandler userApprovalHandler(TokenStore tokenStore){
		TokenStoreUserApprovalHandler handler = new TokenStoreUserApprovalHandler();
		handler.setTokenStore(tokenStore);
		handler.setRequestFactory(new DefaultOAuth2RequestFactory(clientDetailsService));
		handler.setClientDetailsService(clientDetailsService);
		return handler;
	}
	
	@Bean
	@Autowired
	public ApprovalStore approvalStore(TokenStore tokenStore) throws Exception {
		TokenApprovalStore store = new TokenApprovalStore();
		store.setTokenStore(tokenStore);
		return store;
	}
	
}
