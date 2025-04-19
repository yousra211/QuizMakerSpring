package pfe.quiz.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

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

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,property = "id")
public class Exam {
	@Id
	 @GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;
	String title;
	String description;
	float duration;
	String uniqueLink;

	@ManyToOne
	  @JoinColumn(name = "creator_id", nullable = true)
	Creator creator;
	@JsonIgnore
	@OneToMany(mappedBy= "exam")
	  List<Question> question;

}
