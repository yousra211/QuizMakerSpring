package pfe.quiz.service;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
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
/*
	public Creator updateCreator(Creator creator) {
		return creatorRepository.save(creator);
	} 

*/public Creator updateCreatorWithImage(Long id, String fullname, String username, String email, String photoUrl) {
    Creator existingCreator = creatorRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Creator not found with id: " + id));

        existingCreator.setFullname(fullname);
        existingCreator.setUsername(username);
        existingCreator.setEmail(email);
        existingCreator.setPhotoUrl(photoUrl); // Màj image

        return creatorRepository.save(existingCreator);
    }

	
	public Creator updateCreator(Long id, Creator creator) {
	    Optional<Creator> existingCreatorOpt = getCreatorById(id);
	    
	    if (existingCreatorOpt.isPresent()) {
	        Creator existingCreator = existingCreatorOpt.get();
	        
	        existingCreator.setFullname(creator.getFullname());
	        existingCreator.setUsername(creator.getUsername());
	        existingCreator.setEmail(creator.getEmail());
	        
	        // Vérifier si une nouvelle URL d'image est fournie
	        if (creator.getPhotoUrl() != null && !creator.getPhotoUrl().isEmpty()) {
	            existingCreator.setPhotoUrl(creator.getPhotoUrl());
	        }
	        
	        // Autres champs à mettre à jour si nécessaire...
	        
	        return creatorRepository.save(existingCreator);
	    } else {
	        throw new RuntimeException("Creator not found with id: " + id);
	    }
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
       
        return creatorRepository.save(creator);
    }

}