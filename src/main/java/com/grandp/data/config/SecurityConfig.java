package com.grandp.data.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserAuthenticationEntryPoint userAuthenticationEntryPoint;
    private final UserAuthenticationProvider userAuthenticationProvider;

    public SecurityConfig(UserAuthenticationEntryPoint userAuthenticationEntryPoint,
                          UserAuthenticationProvider userAuthenticationProvider) {
        this.userAuthenticationEntryPoint = userAuthenticationEntryPoint;
        this.userAuthenticationProvider = userAuthenticationProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
//                .exceptionHandling().authenticationEntryPoint(userAuthenticationEntryPoint)
//                .and()
                .csrf().disable()
                .authorizeHttpRequests().requestMatchers("/**").permitAll()
//                .and()
//                .authorizeHttpRequests().requestMatchers("/*/*").permitAll()
//                .and()
//                .authorizeHttpRequests().requestMatchers("/*/*/*").permitAll()
//                .and()
//                .authorizeHttpRequests().requestMatchers("/*/*/*/*").permitAll()
//                .and()
//                .authorizeHttpRequests().requestMatchers("/*/*/*/*/*").permitAll();



        ;
//        http
//                .exceptionHandling().authenticationEntryPoint(userAuthenticationEntryPoint)
//                .and()
//                .addFilterBefore(new UsernamePasswordAuthFilter(userAuthenticationProvider), BasicAuthenticationFilter.class)
//                .addFilterBefore(new JwtAuthFilter(userAuthenticationProvider), UsernamePasswordAuthFilter.class)
//                .csrf().disable()
//                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                .and()
//                .authorizeHttpRequests((requests) -> requests
////                        .requestMatchers(HttpMethod.POST, "/v1/signIn", "/v1/signUp").permitAll()
//                        .requestMatchers("/student").permitAll()
//                        .requestMatchers("/favicon.ico").permitAll()
//                        .anyRequest().authenticated())
//                .formLogin()
//	                .loginPage("/login")
//	                .loginProcessingUrl("/login")
//	                .successForwardUrl("/student")
//	                .permitAll()
//	                ;
        
        return http.build();
    }
}
