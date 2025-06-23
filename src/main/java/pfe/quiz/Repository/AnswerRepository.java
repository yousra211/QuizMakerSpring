package pfe.quiz.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import pfe.quiz.model.Answer;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {

//	List<Answer> findAllAnswersByQuestionId(Long idQuestion);
	/*@Query("SELECT COALESCE(SUM(q.grade), 0.0) FROM Answer a " +
		       "JOIN a.question q " +
		       "WHERE a.participant.id = :participantId AND a.isCorrect = true")
		Double calculateTotalScore(@Param("participantId") Long participantId);
*/
	// 🔍 MÉTHODES POUR DIAGNOSTIC
    long countByParticipantId(Long participantId);
    
    List<Answer> findByParticipantId(Long participantId);
    
    // 🔍 OPTIONNEL: Pour vérifier si c'est un problème de doublons
    @Query("SELECT a FROM Answer a WHERE a.participant.id = :participantId ORDER BY a.id DESC")
    List<Answer> findByParticipantIdOrderByIdDesc(@Param("participantId") Long participantId);
    
// pour l,interface exam-resulrs
    @Query("SELECT a FROM Answer a JOIN a.question q WHERE q.exam.id = :examId")
	List<Answer> findByExamId(@Param("examId")Long examId);

}
