package pfe.quiz.controller;

import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pfe.quiz.model.Creator;
import pfe.quiz.model.LoginRequest;
import pfe.quiz.model.RegisterRequest;
import pfe.quiz.service.CreatorService;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {
    
	      @Autowired
	    private PasswordEncoder passwordEncoder;
	   
	    @Autowired
	    private CreatorService creatorService;
	    
	 
	        @GetMapping("/auth/currentUser")
	        public Creator getCurrentUser(Authentication authentication) {
	            if (authentication != null) {
	                return (Creator) authentication.getPrincipal();
	            }
	            return null;
	        }


	    @PostMapping("/login")
	    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
	    	Creator creator = creatorService.findByEmail(request.getEmail()).orElse(null);

	        if (creator == null || !passwordEncoder.matches(request.getPassword(), creator.getPassword())) {
	            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Email ou mot de passe invalide");
	        }
	        creator.setUsername(creator.getUsername());
	        return ResponseEntity.ok(creator); 
	    }

	    
	    //signup
	    @PostMapping("/register")
	    public ResponseEntity<?> register(@RequestBody Creator creator) {
	        if (creatorService.existsByEmail(creator.getEmail())) {
	            return ResponseEntity.badRequest().body("Cet email est déjà utilisé");
	        }
	        
	        Creator savedCreator = creatorService.registerNewCreator(creator);
	        
	        return ResponseEntity.ok(savedCreator); 
	    }
	}

