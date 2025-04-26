
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

import java.util.List;

import pfe.quiz.Repository.CreatorRepository;
import pfe.quiz.service.CreatorService;


@Configuration


public class SecurityConfig {
	@Autowired CreatorRepository creatorRepository;
	
	
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
    	DaoAuthenticationProvider dap=new DaoAuthenticationProvider();
    dap.setUserDetailsService(userDetailsService);
    dap.setPasswordEncoder(encoder());
    return dap;
    }
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> 
        auth.requestMatchers("/home/**").permitAll()
        .requestMatchers("/auth/**").permitAll()
        .requestMatchers("/creators/**").permitAll()
        .requestMatchers("/creator/**").hasRole("CREATOR")
        .requestMatchers("/questions/**").hasRole("CREATOR")
        .requestMatchers("/exams/**").hasRole("CREATOR")
        .anyRequest().authenticated()
        )
        .csrf(csrf -> csrf.disable())
        .cors(withDefaults())
        .formLogin(form -> form.disable()) 
        .httpBasic(withDefaults());
        
        return http.build();
    }

}
