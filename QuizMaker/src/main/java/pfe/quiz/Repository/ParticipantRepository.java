package pfe.quiz.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pfe.quiz.model.Creator;
import pfe.quiz.model.Participant;

@Repository
public interface ParticipantRepository extends JpaRepository<Participant, Long>{

	 
	 

}
