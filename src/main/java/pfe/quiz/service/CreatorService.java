package pfe.quiz.service;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;



import pfe.quiz.Repository.CreatorRepository;
import pfe.quiz.model.Creator;

@Service
public class CreatorService  {

//	SignupRequest signupRequest = new SignupRequest();
  
@Autowired CreatorRepository creatorRepository;

	public List<Creator>getAllCreators(){
		return creatorRepository.findAll();
	}

	public Creator addCreator(Creator creator) {
		  return creatorRepository.save(creator);
		 }

	public Optional<Creator> getCreatorById(Long id){
		return creatorRepository.findById(id);
	}
	
	public void deleteCreatorById(Long id){
		creatorRepository.deleteById(id);
	
	}

	public Creator updateCreator(Creator creator) {
		return creatorRepository.save(creator);
	} 
/*
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		
	    Creator creator = creatorRepository.findByEmail(email)
	            .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
	        
	        return creator; 
	    }
	   public boolean existsByEmail(String email) {
	        return creatorRepository.findByEmail(email).isPresent();
	    }

public Creator registerNewCreator(Creator creator, PasswordEncoder passwordEncoder) {
    creator.setPassword(passwordEncoder.encode(creator.getPassword()));
    creator.setRoles("ROLE_CREATOR");
    creator.setActive(true);
    creator.setPhotoUrl("http://localhost:8080/photos/default.png"); // par défaut
    return creatorRepository.save(creator);
}
}
	/*
	  public Creator registerCreator(SignupRequest signupRequest) {
	        // Vérifier si l'email existe déjà
	        if (creatorRepository.existsByEmail(signupRequest.getEmail())) {
	            throw new RuntimeException("Email déjà utilisé");
	        
	        }
	 // Créer un nouveau creator
    Creator creator = new Creator();
    creator.setFullname(signupRequest.getFullname());
    creator.setEmail(signupRequest.getEmail());
    creator.setPassword(signupRequest.getPassword()); 
    

    return creatorRepository.save(creator);
	        }
    
    
    */
 // vérifier si un email existe déjà
    public boolean existsByEmail(String email) {
        return creatorRepository.findByEmail(email).isPresent();
    }
    
  
    public Optional<Creator> findByEmail(String email) {
        return creatorRepository.findByEmail(email);
    }
	
    public Creator registerNewCreator(Creator creator) {
        creator.setRoles("ROLE_CREATOR");
        creator.setActive(true);
        creator.setPhotoUrl("http://localhost:8080/photos/default.png"); // par défaut
        return creatorRepository.save(creator);
    }

}
