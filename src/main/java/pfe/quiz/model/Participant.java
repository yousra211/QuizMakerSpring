package pfe.quiz.model;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.JoinColumn;

@AllArgsConstructor @NoArgsConstructor @Data
@Entity
public class Participant {

	@Id
	  @GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id ;
	String email;
	String fullname;
	String CIN ;
	@OneToMany(mappedBy= "participant")
	List<Answer> answers;
	
	 Double totalScore; 
}

