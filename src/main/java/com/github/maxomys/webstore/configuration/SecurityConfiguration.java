package com.github.maxomys.webstore.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.maxomys.webstore.auth.JsonAuthenticationFilter;
import com.github.maxomys.webstore.auth.JwtAuthenticationFailureHandler;
import com.github.maxomys.webstore.auth.JwtAuthorizationFilter;
import com.github.maxomys.webstore.auth.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final ObjectMapper objectMapper;

    public SecurityConfiguration(PasswordEncoder passwordEncoder, UserService userService, ObjectMapper objectMapper) {
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
        this.objectMapper = objectMapper;
    }

//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) {
//
//        auth.authenticationProvider(daoAuthenticationProvider());
//    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .cors()
                .and()
                .csrf().disable()
                .headers().frameOptions().disable() //h2 console fix
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .anonymous()
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, "/api/**").permitAll()
                .antMatchers(HttpMethod.POST, "/api/inquiry").permitAll()
                .antMatchers(HttpMethod.POST, "/api/**").hasAnyAuthority("ROLE_ADMIN")
                .and()
                .addFilter(authenticationFilter())
                .addFilterBefore(new JwtAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(userService);
        return provider;
    }

    @Bean
    public JsonAuthenticationFilter authenticationFilter() throws Exception {
        JsonAuthenticationFilter authenticationFilter = new JsonAuthenticationFilter(authenticationManagerBean(), objectMapper);

        authenticationFilter.setAuthenticationFailureHandler(new JwtAuthenticationFailureHandler());
        authenticationFilter.setAuthenticationManager(super.authenticationManagerBean());
        authenticationFilter.setFilterProcessesUrl("/api/login");

        return authenticationFilter;
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Collections.singletonList("*"));
        configuration.setAllowedMethods(Collections.singletonList("*"));
        configuration.setAllowedHeaders(Collections.singletonList("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}
