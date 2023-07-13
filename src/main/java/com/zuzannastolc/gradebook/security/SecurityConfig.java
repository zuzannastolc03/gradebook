package com.zuzannastolc.gradebook.security;

import com.zuzannastolc.gradebook.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http.authorizeHttpRequests(configurer ->
                configurer
                        .requestMatchers(HttpMethod.GET, "/").permitAll()
                        .requestMatchers(HttpMethod.GET, "/teachers").hasRole("TEACHER")
                        .requestMatchers(HttpMethod.GET, "/students").hasRole("STUDENT")
                        .requestMatchers(HttpMethod.GET, "/logged_username").authenticated()
                        .requestMatchers(HttpMethod.GET, "/logged_authorities").authenticated()
                        .requestMatchers(HttpMethod.POST, "/addStudent").hasRole("TEACHER")
                        .requestMatchers(HttpMethod.POST, "/addTeacher").hasRole("HEADTEACHER")
                        .requestMatchers(HttpMethod.POST, "/register_user").hasRole("HEADTEACHER"));

        http.httpBasic(Customizer.withDefaults());
        http.csrf(AbstractHttpConfigurer::disable);
        return http.build();
    }
}
