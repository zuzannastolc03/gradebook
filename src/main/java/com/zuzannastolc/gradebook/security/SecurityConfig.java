package com.zuzannastolc.gradebook.security;
import com.zuzannastolc.gradebook.service.AppService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;


@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }


    @Bean
    public DaoAuthenticationProvider authenticationProvider(AppService appService) {
        DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
        auth.setUserDetailsService(appService);
        auth.setPasswordEncoder(passwordEncoder());
        return auth;
    }

    @Bean
    public UserDetailsManager userDetailsManager(DataSource dataSource){
        JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);
        jdbcUserDetailsManager.setUsersByUsernameQuery(
                "select username, password, enabled from users where username=?"
        );

        jdbcUserDetailsManager.setAuthoritiesByUsernameQuery(
                "SELECT usr.username, authority.authority " +
                        "FROM users usr, authorities authority " +
                        "WHERE usr.username = ? " +
                        "and usr.user_id = authority.user_id"
        );
        return jdbcUserDetailsManager;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http.authorizeHttpRequests(configurer ->
                configurer
                        .requestMatchers(HttpMethod.GET, "/v3/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/swagger-ui/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/").permitAll()
                        .requestMatchers(HttpMethod.GET, "/teachers").hasRole("TEACHER")
                        .requestMatchers(HttpMethod.GET, "/students").hasRole("STUDENT")
                        .requestMatchers(HttpMethod.GET, "/logged_username").authenticated()
                        .requestMatchers(HttpMethod.GET, "/logged_authorities").authenticated()
                        .requestMatchers(HttpMethod.POST, "/add_new_user").hasRole("HEADTEACHER")
                        .requestMatchers(HttpMethod.POST, "/add_new_student").hasRole("TEACHER")
                        .requestMatchers(HttpMethod.POST, "/add_new_teacher").hasRole("HEADTEACHER")
                        .requestMatchers(HttpMethod.PUT, "/disable_user").hasRole("HEADTEACHER")
                        .requestMatchers(HttpMethod.PUT, "/change_password").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/update_student").hasRole("HEADTEACHER")
                        .requestMatchers(HttpMethod.PUT, "/update_teacher").hasRole("HEADTEACHER")
                        .requestMatchers(HttpMethod.POST, "/add_new_class").hasRole("HEADTEACHER")
                        .requestMatchers(HttpMethod.GET, "/classes_list").hasRole("TEACHER")
                        .requestMatchers(HttpMethod.GET, "/students_in_class").hasRole("TEACHER")
                        .requestMatchers(HttpMethod.POST, "/add_new_subject").hasRole("HEADTEACHER")
                        .requestMatchers(HttpMethod.POST, "/assign_teacher_to_subject").hasRole("HEADTEACHER")
                        .requestMatchers(HttpMethod.GET, "/list_of_teachers_subjects").hasRole("TEACHER")
                        .requestMatchers(HttpMethod.GET, "/list_of_subjects_teachers").authenticated()
                        .requestMatchers(HttpMethod.POST, "/assign_class_to_subject").hasRole("HEADTEACHER")
                        .requestMatchers(HttpMethod.GET, "/list_of_classes_subjects").hasRole("STUDENT")
                        .requestMatchers(HttpMethod.GET, "/list_of_subjects_classes").hasRole("TEACHER")
                        .requestMatchers(HttpMethod.GET, "/list_of_my_grades_from_subject").hasRole("STUDENT")
                        .requestMatchers(HttpMethod.GET, "/list_of_students_grades_from_subject").hasRole("TEACHER")
                        .requestMatchers(HttpMethod.POST, "/add_new_grade").hasRole("TEACHER")
                        .requestMatchers(HttpMethod.PUT, "/update_grade").hasRole("TEACHER")
                        .requestMatchers(HttpMethod.DELETE, "/delete_grade").hasRole("TEACHER")
                        .requestMatchers(HttpMethod.GET, "/my_mean_from_subject").hasRole("STUDENT")
                        .requestMatchers(HttpMethod.GET, "/mean_of_students_grades_from_subject").hasRole("TEACHER"));

        http.httpBasic(Customizer.withDefaults());
        http.csrf(AbstractHttpConfigurer::disable);
        return http.build();
    }
}
