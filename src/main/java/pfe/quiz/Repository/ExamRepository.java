package pfe.quiz.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import pfe.quiz.model.Answer;
import pfe.quiz.model.Creator;
import pfe.quiz.model.Exam;
import pfe.quiz.model.Question;

@Repository
public interface ExamRepository extends JpaRepository<Exam, Long>{
	

	 List<Exam> findExamsByCreatorUsername(String username);

	 @Query("SELECT q FROM Exam e JOIN e.question q WHERE e.id = :idExam")
	    List<Question> findAllQuestionsByExamId(@Param("idExam") Long idExam);
	    
	    // Méthode pour trouver les réponses (à adapter selon votre modèle)
	    @Query("SELECT a FROM Exam e JOIN e.question q JOIN q.answers a WHERE e.id = :idExam")
	    List<Answer> findAllAnswersByExamId(@Param("idExam") Long idExam);

		List<Exam> findByCreator(Creator creator);
	}