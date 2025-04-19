
package pfe.quiz.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import static org.springframework.security.config.Customizer.withDefaults;

import java.util.Arrays;

import pfe.quiz.service.CreatorService;


@Configuration

public class SecurityConfig {

	@Autowired
	CreatorService creatorService;
	
	@Bean
	public PasswordEncoder encoder() {
		return new BCryptPasswordEncoder();
	}
	
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
    	DaoAuthenticationProvider dap=new DaoAuthenticationProvider();
    dap.setUserDetailsService(creatorService);
    dap.setPasswordEncoder(encoder());
    return dap;
    }
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> 
        auth.requestMatchers("/home/**").permitAll()
        .requestMatchers("/auth/**").permitAll()
        .requestMatchers("/creators/**").permitAll()
        .requestMatchers("/questions/**").hasRole("CREATOR")
        .requestMatchers("/exams/**").permitAll()
        .anyRequest().authenticated()
        )
        .csrf(csrf -> csrf.disable())
        .cors(withDefaults())
        .formLogin(form -> form.disable()) // Désactiver le formulaire de connexion par défaut
        .httpBasic(withDefaults());
        
        return http.build();
    }
    /*
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> 
        auth.requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.POST, "/creators")).permitAll()
        .requestMatchers(AntPathRequestMatcher.antMatcher("/home/**")).permitAll()
        .requestMatchers(AntPathRequestMatcher.antMatcher("/auth/**")).permitAll()
        .requestMatchers(AntPathRequestMatcher.antMatcher("/creators/**")).permitAll()
        .requestMatchers(AntPathRequestMatcher.antMatcher("/questions/**")).hasRole("CREATOR")
        .requestMatchers(AntPathRequestMatcher.antMatcher("/exams/**")).hasRole("CREATOR")
        .anyRequest().authenticated()
        )
        .csrf(csrf -> csrf.disable())
        .cors(withDefaults())
        .formLogin(form -> form.disable()) // Désactiver le formulaire de connexion par défaut
        .httpBasic(withDefaults());
        
        http.authenticationProvider(daoAuthenticationProvider());
        
        return http.build();
    }
    
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:4200")); // URL de ton frontend
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true); // utile si tu envoies des cookies/session
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
   */
    
}
