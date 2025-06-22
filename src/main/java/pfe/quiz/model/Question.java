package pfe.quiz.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
private Long id;
    
    @Column(name = "text", nullable = false, length = 1000)
    private String text;
    
    @Column(name = "type", nullable = false)
    private String type; // "directe" ou "QCM"
    
    @Column(name = "response", length = 500)
    private String response; // pour stocker la bonne réponse pour une question directe
    
    @Column(name = "options", columnDefinition = "TEXT")
    private String options; // Stockage JSON des options pour les QCM
    
    @Column(name = "grade", nullable = false)
    private Integer grade;
    
  
    /*
    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Answer> answers = new ArrayList<>();
    */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exam_id")
    @JsonIgnore// Évite la boucle infinie
    private Exam exam;
    
    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore // ← AJOUTEZ CETTE ANNOTATION
    private List<Answer> answers;
}



