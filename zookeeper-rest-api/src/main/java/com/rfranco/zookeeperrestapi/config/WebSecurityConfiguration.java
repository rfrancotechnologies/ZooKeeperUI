package com.rfranco.zookeeperrestapi.config;

import com.rfranco.zookeeperrestapi.config.users.ConfiguredUsers;
import com.rfranco.zookeeperrestapi.filters.JWTAuthenticationFilter;
import com.rfranco.zookeeperrestapi.services.CookieAuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.Http401AuthenticationEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Autowired
    private CookieAuthenticationService cookieAuthenticationService;

    @Autowired
    private ConfiguredUsers configuredUsers;

    @Autowired
    private Http401AuthenticationEntryPoint authEntrypoint;

    @Value("${cors.allowed_origins}")
    private String corsAllowedOrigins;

    @Value("${cors.allowed_headers}")
    private String corsAllowedHeaders;

    @Value("${cors.allowed_methods}")
    private String corsAllowedMethods;

    @Bean
    public Http401AuthenticationEntryPoint securityException401EntryPoint(){
        return new Http401AuthenticationEntryPoint("Bearer realm=\"webrealm\"");
    }

    @Bean
    public UserDetailsService getUserDetailsService() {
        InMemoryUserDetailsManager inMemoryUserDetailsService = new InMemoryUserDetailsManager();
        configuredUsers.getUsersDetails().forEach(inMemoryUserDetailsService::createUser);
        return inMemoryUserDetailsService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .cors().configurationSource(getCorsConfiguration()).and()
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, "/api/sessions").permitAll()
                .antMatchers("/api/**").authenticated()
                .antMatchers(HttpMethod.GET, "/**").permitAll()
                .and()
                .addFilterBefore(new JWTAuthenticationFilter(cookieAuthenticationService),
                        UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling().authenticationEntryPoint(authEntrypoint);
    }

    private UrlBasedCorsConfigurationSource getCorsConfiguration() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin(corsAllowedOrigins);
        config.addAllowedHeader(corsAllowedHeaders);
        config.addAllowedMethod(corsAllowedMethods);
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}