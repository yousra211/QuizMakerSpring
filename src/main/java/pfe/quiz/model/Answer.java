package pfe.quiz.model;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Answer {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;
	String text;
	Boolean isCorrect;
	
	@ManyToOne
	@JoinColumn(name = "question_id")  
	Question question;
	
	
	 @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "participant_id")
    
	 Participant participant;
} 
