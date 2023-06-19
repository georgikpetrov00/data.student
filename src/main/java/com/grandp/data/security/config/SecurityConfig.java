package com.grandp.data.security.config;

import com.grandp.data.entity.authority.SimpleAuthority;
import com.grandp.data.security.handler.AuthenticationSuccessHandler;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeHttpRequests().requestMatchers("/**").permitAll()
        ;
        return http.build();
    }

//    @Bean
//    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .authorizeRequests()
//                    .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
//                    .requestMatchers("/user/**").hasRole(SimpleAuthority.STUDENT.getName())
//                    .requestMatchers("/subject").hasRole(SimpleAuthority.ADMINISTRATOR.getName())
//                    .requestMatchers("/login", "/logout", "/error").permitAll()
//                    .anyRequest().permitAll()
//                .and()
//                    .exceptionHandling()
//                        .accessDeniedHandler((request, response, accessDeniedException) -> {
//                            request.setAttribute("errorMessage", "You do not have permission to access this resource.");
////                            response.sendRedirect("/error");
//                            throw accessDeniedException;
//                        })
//                .and()
//                    .formLogin()
//                    .successHandler(new AuthenticationSuccessHandler()) //FIXME prevent accessing /login after authentication
//                    .loginPage("/login")
//                    .usernameParameter(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY)
//                    .passwordParameter(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_PASSWORD_KEY)
//                    .defaultSuccessUrl("/profile")
//                    .failureForwardUrl("/login-error")
//                .and()
//                    .logout()
//                    .logoutUrl("/logout")
//                    .logoutSuccessUrl("/")
//                    .deleteCookies("JSESSIONID")
//                    .invalidateHttpSession(true)
//                    .clearAuthentication(true);
//
//        http.exceptionHandling().accessDeniedPage("/error");
//
//        return http.build();
//    }

}
