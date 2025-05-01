package pfe.quiz.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class Question {
	@Id
	 @GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;
	String text;
	String type; // "directe" ou "QCM"
	String response; 
	private Integer grade;
	 @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
	    private List<Answer> answers = new ArrayList<>();

	  @ManyToOne
	  @JoinColumn(name = "exam_id")
	    Exam exam;

}
