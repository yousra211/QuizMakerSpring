package pfe.quiz.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
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
	@Column(name = "is_correct")
	Boolean isCorrect;
	
	@ManyToOne(fetch = FetchType.LAZY)
	 @JoinColumn(name = "question_id", nullable = false) // Assurez-vous que la colonne est bien mappée
    @JsonIgnore // Évite la boucle infinie
    private Question question;
	
	
	 @ManyToOne(fetch = FetchType.LAZY)
	    @JoinColumn(name = "participant_id", nullable = false)
	 @JsonIgnore
	    private Participant participant;
	    
	 @JsonProperty("questionId")
	    public Long getQuestionId() {
	        return question != null ? question.getId() : null;
	    }
} 
