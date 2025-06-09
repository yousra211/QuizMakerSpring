package pfe.quiz.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pfe.quiz.model.Answer;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {

//	List<Answer> findAllAnswersByQuestionId(Long idQuestion);

}
