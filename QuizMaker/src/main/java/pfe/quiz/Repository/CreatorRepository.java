package pfe.quiz.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import pfe.quiz.model.Creator;

@Repository
public interface CreatorRepository extends JpaRepository<Creator, Long>{

	
	 
	 UserDetails findByUsername(String username);

	 @Query("SELECT c FROM Creator c WHERE c.username = :username")
	    Creator findCreatorByUsername(@Param("username") String username);
	 
	 Optional<Creator> findByEmail(String email);

	 boolean existsByEmail(String email);
}
