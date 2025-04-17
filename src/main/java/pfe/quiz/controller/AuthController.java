package pfe.quiz.controller;

import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.CrossOrigin;
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
   /*
	  @Autowired
	    private AuthenticationManager authenticationManager;
	    
	    
	      @Autowired
	    private PasswordEncoder passwordEncoder;
	    */
	    
	    @Autowired
	    private CreatorService creatorService;
	    
	  

	    // Définition du record pour la requête de login (interne au contrôleur)
	    public record LoginRequest(String email, String password) {}

	    // Définition du record pour la requête d'inscription
	    public record RegisterRequest(
	        String fullname,
	        String username,
	        String email,
	        String password
	    ) {}
/*
	    // ========== LOGIN ==========
	    @PostMapping("/login")
	    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
	        try {
	            Authentication authentication = authenticationManager.authenticate(
	                new UsernamePasswordAuthenticationToken(
	                    request.email(),
	                    request.password()
	                )
	            );
	            
	            SecurityContextHolder.getContext().setAuthentication(authentication);
	            Creator creator = (Creator) authentication.getPrincipal();
	            
	            // Retourne uniquement les données nécessaires (évite @JsonIgnore)
	            return ResponseEntity.ok(new Object() {
	                public Long id = creator.getId();
	                public String fullname = creator.getFullname();
	                public String username = creator.getUsername();
	                public String email = creator.getEmail();
	                public String photoUrl = creator.getPhotoUrl();
	                public String roles = creator.getRolesString();
	            });
	        } catch (BadCredentialsException e) {
	            return ResponseEntity.status(401).body("Email ou mot de passe incorrect");
	        } catch (Exception e) {
	            return ResponseEntity.status(500).body("Erreur lors de la connexion: " + e.getMessage());
	        }
	    }
*/

	    // ========== REGISTER ========
	    @PostMapping("/register")
	    public ResponseEntity<?> register(@RequestBody Creator creator) {
	        if (creatorService.existsByEmail(creator.getEmail())) {
	            return ResponseEntity.badRequest().body("Cet email est déjà utilisé");
	        }
	        
	        Creator savedCreator = creatorService.registerNewCreator(creator);
	        
	        return ResponseEntity.ok(savedCreator); 
	    }
	}
