package edziekanat.isi;

import edziekanat.isi.misc.AuthenticationFilter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    /*
    @Bean
    public InMemoryUserDetailsManager users() {

        UserDetails user = User.withDefaultPasswordEncoder()
                .username("john")
                .password("password")
                .roles("USER")
                .build();

        return new InMemoryUserDetailsManager(user);
    }
     */

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationManager authManager) throws Exception {

//     .authorizeHttpRequests(auth ->
//                auth.requestMatchers("/adming").hasRole("adming").anyRequest().authenticated())
//make roles have sense
        AuthenticationFilter filter =
                new AuthenticationFilter(authManager);
        CorsConfig corsConfig = new CorsConfig();
        http
            .cors(cors -> corsConfig.corsConfigurationSource()) // gets added automatically
            .sessionManagement(session -> session
                    .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                    .invalidSessionUrl("/login")
            )
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/auth/**", "/user/register", "/swagger-ui/**", "/v3/api-docs/**", "/send", "/form/**", "/payment/notify").permitAll()
                .anyRequest()
                .authenticated())
            .addFilterAt(filter, UsernamePasswordAuthenticationFilter.class)
            .formLogin(form -> form.disable())
            .logout(logout -> logout
                    .logoutSuccessHandler((request, response, authentication) -> {
                        response.setStatus(HttpServletResponse.SC_OK);
                        response.setContentType("application/json");
                    })
                    .logoutUrl("/auth/logout")
                    .invalidateHttpSession(true)
                    .clearAuthentication(true)
                    .deleteCookies("JSESSIONID"));

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config)
            throws Exception {

        return config.getAuthenticationManager();
    }
}