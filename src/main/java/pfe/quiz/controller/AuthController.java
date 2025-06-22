package pfe.quiz.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pfe.quiz.model.Creator;
import pfe.quiz.model.JwtResponse;
import pfe.quiz.model.CreatorResponse;
import pfe.quiz.model.LoginRequest;
import pfe.quiz.model.RegisterRequest;
import pfe.quiz.service.CreatorService;
import pfe.quiz.service.JwtService;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {
	 @Autowired
	    private AuthenticationManager authenticationManager;
	    
	    @Autowired
	    private JwtService jwtService;
	    
	   
	    @Autowired
	    private CreatorService creatorService;
	    
	 
	    @GetMapping("/currentUser")
	    public Creator getCurrentUser(Authentication authentication) {
	        if (authentication != null) {
	            return (Creator) authentication.getPrincipal();
	        }
	        return null;
	    }
	    
	    

	    @PostMapping("/login")
	    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
	        try {
	            // Vérifier si l'utilisateur existe d'abord
	            Creator creator = creatorService.findByEmail(loginRequest.getEmail())
	                    .orElse(null);
	            
	            if (creator == null) {
	                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
	                        .body("Email ou mot de passe invalide");
	            }
	            
	            // Vérifier si le compte est actif
	            if (!creator.isActive()) {
	                return ResponseEntity.status(HttpStatus.FORBIDDEN)
	                        .body("Votre compte est temporairement désactivé. Veuillez contacter l'administrateur à : admin@admin.com");
	            }

	            // Authentifier l'utilisateur
	            Authentication authentication = authenticationManager.authenticate(
	                new UsernamePasswordAuthenticationToken(
	                    loginRequest.getEmail(),
	                    loginRequest.getPassword()
	                )
	            );

	            // Si l'authentification réussit, générer le token JWT
	            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
	            String token = jwtService.generateToken(userDetails);

	            // Créer la réponse JWT
	            CreatorResponse creatorResponse = new CreatorResponse(creator);
	            JwtResponse response = new JwtResponse(token ,"Bearer" , creatorResponse);
	            return ResponseEntity.ok(response);

	            
	        } catch (BadCredentialsException e) {
	            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
	                    .body("Email ou mot de passe invalide");
	        } catch (Exception e) {
	            return ResponseEntity.badRequest()
	                    .body("Erreur d'authentification: " + e.getMessage());
	        }
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
	    
	    @PostMapping("/register-admin")
	    public ResponseEntity<?> registerAdmin(@RequestBody Creator creator) {
	        creator.setRoles("ROLE_ADMIN");
	        return ResponseEntity.ok(creatorService.registerNewCreator(creator));
	    }

	    
	}

