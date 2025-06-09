
package pfe.quiz.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import static org.springframework.security.config.Customizer.withDefaults;

import java.util.Arrays;
import java.util.List;

import pfe.quiz.Repository.CreatorRepository;
import pfe.quiz.service.CreatorService;


@Configuration
public class SecurityConfig {
    
    @Autowired 
    CreatorRepository creatorRepository;
    
    @Bean
    public UserDetailsService userDetailsService(CreatorRepository creatorRepository) {
        return email -> creatorRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
    
    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(UserDetailsService userDetailsService) {
        DaoAuthenticationProvider dap = new DaoAuthenticationProvider();
        dap.setUserDetailsService(userDetailsService);
        dap.setPasswordEncoder(encoder());
        return dap;
    }
    
    // Configuration CORS - C'est ce qui manquait !
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        
        // Autoriser Angular sur le port 4200
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:4200"));
        
        // Autoriser toutes les méthodes HTTP nécessaires
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        
        // Autoriser tous les headers
        configuration.setAllowedHeaders(Arrays.asList("*"));
        
        // Autoriser les credentials (cookies, authorization headers)
        configuration.setAllowCredentials(true);
        
        // Exposer les headers de réponse au frontend
        configuration.setExposedHeaders(Arrays.asList("Authorization", "Content-Type"));
        
        // Durée de cache pour les requêtes preflight
        configuration.setMaxAge(3600L);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        
        return source;
    }
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> 
            auth.requestMatchers("/home/**").permitAll()
                .requestMatchers("/auth/**").permitAll()
                .requestMatchers("/creators/**").hasRole("CREATOR")
                .requestMatchers("/creator/**").hasRole("CREATOR")
                .requestMatchers("/questions/**").hasRole("CREATOR")
                .requestMatchers("/exams/**").hasRole("CREATOR")
                
                .anyRequest().authenticated()
        )
        .csrf(csrf -> csrf.disable())
        .cors(cors -> cors.configurationSource(corsConfigurationSource())) // Utiliser notre configuration CORS
        .formLogin(form -> form.disable()) 
        .httpBasic(withDefaults());
        
        return http.build();
    }
}