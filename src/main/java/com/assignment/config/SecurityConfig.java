package com.assignment.config;

import com.assignment.dto.AppUser;
import com.assignment.dto.AppUserWrapper;
import com.assignment.dto.ClientsWrapper;
import com.assignment.util.ResourceJsonReader;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class SecurityConfig {
    @Bean
    PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }

    @Bean
    UserDetailsService users(PasswordEncoder enc) {
        ResourceJsonReader resourceJsonReader = new ResourceJsonReader(new ObjectMapper());
        AppUserWrapper appUserWrapper = resourceJsonReader.readJsonFile("users.json",new TypeReference<AppUserWrapper>() {});
        List<AppUser> appUsers = appUserWrapper.getAppUsers();

        List<UserDetails> userDetails = new ArrayList<>();

        if (appUsers != null && !appUsers.isEmpty()) {
            for (AppUser appUser : appUsers) {
                UserDetails details = User.withUsername(appUser.getFullName())
                        .password(enc.encode(appUser.getPassword()))
                        .roles(appUser.getRole())
                        .build();
                userDetails.add(details);
            }
        }

        return new InMemoryUserDetailsManager(userDetails);
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable());
        http.httpBasic(Customizer.withDefaults());
        http.authorizeHttpRequests(auth -> auth
                // Allow Swagger & H2 console without auth
                .requestMatchers("/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                .requestMatchers("/h2-console/**").permitAll()

                // API role restrictions
                .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/v1/clients/**")
                    .hasAnyRole("VIEWER", "EDITOR", "ADMIN")
                .requestMatchers(org.springframework.http.HttpMethod.POST, "/api/v1/clients/**")
                    .hasAnyRole("EDITOR", "ADMIN")
                .requestMatchers(org.springframework.http.HttpMethod.PUT, "/api/v1/clients/**")
                    .hasAnyRole("EDITOR", "ADMIN")
                .requestMatchers(org.springframework.http.HttpMethod.DELETE, "/api/v1/clients/**")
                    .hasRole("ADMIN")

                // Everything else requires authentication
                .anyRequest().authenticated()
        );
        return http.build();
    }

}
