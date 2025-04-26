package pfe.quiz.service;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;



import pfe.quiz.Repository.CreatorRepository;
import pfe.quiz.model.Creator;

@Service
public class CreatorService implements UserDetailsService {

//	SignupRequest signupRequest = new SignupRequest();
  
@Autowired CreatorRepository creatorRepository;

@Autowired
private PasswordEncoder passwordEncoder;

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

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
	        return creatorRepository.findByUsername(username); 
	    }
	
	
 // vérifier si un email existe déjà
    public boolean existsByEmail(String email) {
        return creatorRepository.findByEmail(email).isPresent();
    }
    
  
    public Optional<Creator> findByEmail(String email) {
        return creatorRepository.findByEmail(email);
    }
	
    public Creator registerNewCreator(Creator creator) {
        creator.setRoles("ROLE_CREATOR");
        String encodedPassword = passwordEncoder.encode(creator.getPassword());
        creator.setPassword(encodedPassword);
        creator.setActive(true);
        creator.setPhotoUrl("/QuizMaker/src/main/java/photos/WhatsApp Image 2025-04-18 at 14.13.04.jpeg");
        return creatorRepository.save(creator);
    }

}
