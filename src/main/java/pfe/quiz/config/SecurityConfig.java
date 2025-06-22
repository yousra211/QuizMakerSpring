
package pfe.quiz.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import jakarta.servlet.http.HttpServletResponse;

import static org.springframework.security.config.Customizer.withDefaults;

import java.util.Arrays;
import java.util.List;

import pfe.quiz.Repository.CreatorRepository;
import pfe.quiz.service.CreatorService;
import pfe.quiz.jwt.JwtAuthFilter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;





@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    @Autowired 
    CreatorRepository creatorRepository;
    
    @Lazy
    @Autowired
    private JwtAuthFilter jwtAuthFilter;
    
    @Bean
    public UserDetailsService userDetailsService() {
        return email -> creatorRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
    
    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider dap = new DaoAuthenticationProvider();
        dap.setUserDetailsService(userDetailsService());
        dap.setPasswordEncoder(encoder());
        return dap;
    }
    
    // ✅ CONFIGURATION CORS AMÉLIORÉE
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        
        // Autoriser Angular sur le port 4200
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:4200"));
        
        // Autoriser toutes les méthodes HTTP
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH", "HEAD"));
        
        // Autoriser tous les headers
        configuration.setAllowedHeaders(Arrays.asList("*"));
        
        // Autoriser les credentials
        configuration.setAllowCredentials(true);
        
        // Exposer les headers nécessaires
        configuration.setExposedHeaders(Arrays.asList("Authorization", "Content-Type", "Accept"));
        
        // Cache pour les requêtes preflight
        configuration.setMaxAge(3600L);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        
        return source;
    }
   
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 1. Désactiver CSRF
                .csrf(csrf -> csrf.disable())

                // 2. Configuration CORS
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // 3. ✅ RÈGLES D'AUTORISATION CLAIRES
                .authorizeHttpRequests(auth -> auth
                        // 🔓 ENDPOINTS PUBLICS (pas d'authentification)
                        .requestMatchers(HttpMethod.POST, "/auth/login", "/auth/register").permitAll()
                        .requestMatchers(HttpMethod.GET, "/auth/**").permitAll()
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll() // Preflight CORS
                        .requestMatchers("/error").permitAll()

                        // 🔓 PARTICIPANTS PUBLICS (si c'est votre choix)
                        .requestMatchers(HttpMethod.GET, "/participants/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/participants/**").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/participants/**").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/participants/**").permitAll()
                        
                        // 🔓 AUTRES ENDPOINTS PUBLICS (seulement ceux qui existent côté Spring)
                        .requestMatchers("/home/**").permitAll() // Si vous avez ce contrôleur

                        // 🔒 ENDPOINTS PROTÉGÉS (authentification requise)
                        .requestMatchers("/creators/**").hasRole("CREATOR")
                        .requestMatchers("/questions/**").hasRole("CREATOR")
                        .requestMatchers("/exams/**").hasRole("CREATOR")
                        
                        // 🔒 Toutes les autres routes nécessitent une authentification
                        .anyRequest().authenticated()
                )

                // 4. Session stateless
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // 5. Provider d'authentification
                .authenticationProvider(authenticationProvider())

                // 6. Filtre JWT
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                
                // 7. ✅ GESTION DES EXCEPTIONS D'AUTHENTIFICATION
                .exceptionHandling(exceptions -> exceptions
                    .authenticationEntryPoint((request, response, authException) -> {
                        System.out.println("🚨 Erreur d'authentification: " + authException.getMessage());
                        System.out.println("🚨 URL: " + request.getRequestURL());
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        response.getWriter().write("{\"error\":\"Non autorisé\",\"message\":\"" + authException.getMessage() + "\"}");
                    })
                    .accessDeniedHandler((request, response, accessDeniedException) -> {
                        System.out.println("🚨 Accès refusé: " + accessDeniedException.getMessage());
                        System.out.println("🚨 URL: " + request.getRequestURL());
                        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                        response.getWriter().write("{\"error\":\"Accès refusé\",\"message\":\"" + accessDeniedException.getMessage() + "\"}");
                    })
                );

        return http.build();
    }
    
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}