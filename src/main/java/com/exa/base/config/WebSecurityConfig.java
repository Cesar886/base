package com.exa.base.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private DataSource dataSource;

    @Value("${spring.queries.users-query}")
    private String usersQuery;

    @Value("${spring.queries.roles-query}")
    private String rolesQuery;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication()
            .usersByUsernameQuery("SELECT EMAIL, PASSWORD, ACTIVO FROM USUARIO WHERE EMAIL = ?")
            .authoritiesByUsernameQuery("SELECT U.EMAIL, R.NOMBRE FROM USUARIO U INNER JOIN USUARIO_ROLE RU ON(U.ID = RU.USUARIO_ID) INNER JOIN ROLE R ON(RU.ROLE_ID = R.ID) WHERE U.EMAIL = ?")
            .dataSource(dataSource)
            .passwordEncoder(bCryptPasswordEncoder);
    }
    
    @Bean
    public SecurityFilterChain formLoginFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authorize -> authorize
                    .requestMatchers("/registrar").permitAll()
                    .anyRequest().authenticated())
            .formLogin(form -> form
                    .loginPage("/login")
                    .usernameParameter("email") // Usa email en vez de username
                    .defaultSuccessUrl("/inicio", true)
                    .failureUrl("/login?error=true")
                    .permitAll()
            ).logout(logout -> logout
            .logoutUrl("/logout")
            );
            return http.build();
        }
    
}
