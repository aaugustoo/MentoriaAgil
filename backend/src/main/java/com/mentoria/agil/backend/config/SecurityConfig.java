package com.mentoria.agil.backend.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

// imports para cors config
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Autowired
	SecurityFilter securityFilter;
	
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    	return http
    			.csrf(csrf -> csrf.disable())

				// adição do cors, linha a ser apagada caso não seja necessário para o projeto
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

    			.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
    			.authorizeHttpRequests(authorize -> authorize
    				// endpoints públicos
                	.requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                	.requestMatchers(HttpMethod.POST, "/auth/register").permitAll()
                
                	// endpoitnts de mentor (requerem autenticação)
                	.requestMatchers(HttpMethod.POST, "/api/mentors").authenticated()
                	.requestMatchers(HttpMethod.GET, "/api/mentors").authenticated()
                	.requestMatchers(HttpMethod.GET, "/api/mentors/**").authenticated()
                	.requestMatchers(HttpMethod.PUT, "/api/mentors/**").authenticated()
                	.requestMatchers(HttpMethod.DELETE, "/api/mentors/**").authenticated()
                
                	// endpoints de admin (requerem role ADMIN)
                	.requestMatchers(HttpMethod.POST, "/admin/**").hasRole("ADMIN")
                	.requestMatchers(HttpMethod.GET, "/admin/**").hasRole("ADMIN")
                	.requestMatchers(HttpMethod.PUT, "/admin/**").hasRole("ADMIN")
                	.requestMatchers(HttpMethod.DELETE, "/admin/**").hasRole("ADMIN")
                
                	// demais endpoints
                	.anyRequest().authenticated()
    				)
					//configuração do 401 unauthorized
                    .exceptionHandling(exception -> exception
                    .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                	)
    			.addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
    			.build();
    }

	// adição de método para cors
	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
    	CorsConfiguration configuration = new CorsConfiguration();
    	configuration.setAllowedOrigins(Arrays.asList(
        	"http://localhost:4200",  // Angular
        	"http://localhost:8080"   // Backend
    	));
    	configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE","PATCH", "OPTIONS"));
    	configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
		configuration.setExposedHeaders(Arrays.asList(
            "Authorization"  // Headers expostos para o frontend
        ));
    	configuration.setAllowCredentials(true);
		configuration.setMaxAge(3600L); // Cache de 1 hora
    
    	UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    	source.registerCorsConfiguration("/**", configuration);
    	return source;
	}
}