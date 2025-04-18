package pfe.quiz.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pfe.quiz.model.Question;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long>{

}
