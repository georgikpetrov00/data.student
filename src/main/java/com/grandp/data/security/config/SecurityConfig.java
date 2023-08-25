package com.grandp.data.security.config;

import com.grandp.data.entity.authority.SimpleAuthority;
import com.grandp.data.security.captcha.ReCaptchaFilter;
import com.grandp.data.security.handler.AuthenticationSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf().disable()
//                .authorizeHttpRequests().requestMatchers("/**").permitAll()
//        ;
//        return http.build();
//    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
          .cors(Customizer.withDefaults())
          .csrf().disable()
                .authorizeHttpRequests()
                    .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
//                    .requestMatchers("/user/**").hasRole(SimpleAuthority.STUDENT.getName())
                    .requestMatchers("/profile", "/grades", "/program").hasAuthority("STUDENT")
                    .requestMatchers("/subject").hasRole(SimpleAuthority.ADMINISTRATOR.getName())
                    .requestMatchers("/login", "/logouted", "/error").permitAll()
                    .anyRequest().permitAll()
                .and()
                    .exceptionHandling()
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            request.setAttribute("errorMessage", "You do not have permission to access this resource.");
                            request.setAttribute("exception", accessDeniedException);
                            throw accessDeniedException;
                        })
                .and()
//                    .addFilterBefore(new ReCaptchaFilter(), UsernamePasswordAuthenticationFilter.class)
//                    .addFilterAfter(new ReCaptchaFilter(), UsernamePasswordAuthenticationFilter.class)
                    .formLogin()
                    .successHandler(new AuthenticationSuccessHandler())
                    .loginPage("/login")
                    .usernameParameter(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY)
                    .passwordParameter(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_PASSWORD_KEY)
//                    .and().
                    .defaultSuccessUrl("/profile")
                    .failureForwardUrl("/login-error")
                .and()
                    .logout()
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/logouted")
                    .deleteCookies("JSESSIONID")
                    .invalidateHttpSession(true)
                    .clearAuthentication(true);

        http.exceptionHandling().accessDeniedPage("/error");

        return http.build();
    }

}
