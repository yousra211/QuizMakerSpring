package pfe.quiz.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;

import pfe.quiz.model.Answer;
import pfe.quiz.model.Creator;
import pfe.quiz.model.Exam;
import pfe.quiz.model.Question;
import pfe.quiz.Repository.ExamRepository;
import pfe.quiz.Repository.QuestionRepository;
import pfe.quiz.Repository.AnswerRepository;
import pfe.quiz.Repository.CreatorRepository;

@Service
public class ExamService {

	@Autowired ExamRepository examRepository;
@Autowired CreatorRepository creatorRepository;
@Autowired QuestionRepository questionRepository;
@Autowired AnswerRepository answerRepository;	

	public List<Exam>getAllExams() {
		return examRepository.findAll();
	}

	public Exam addExam(Exam exam) {
		try {
		return examRepository.save(exam);
		}catch (Exception e) {
			 System.err.println("Erreur lors de l'enregistrement de l'examen : " + e.getMessage());
		        e.printStackTrace(); // pour afficher la stack trace complète
		        return null;
		}
		
		
		}
	
	public Optional<Exam> getExamById(Long id) {
		return examRepository.findById(id);
	}

	public void deleteExamById(Long id) {
		examRepository.deleteById(id);
		
	}

	public List<Exam> getExamsByCreator(@PathVariable Long id) {
	    Creator creator = creatorRepository.findCreatorById(id);
	    return examRepository.findByCreator(creator);
	    }

	public Exam addExamToCreator(Long id, Exam exam) {
	    Creator creator = creatorRepository.findCreatorById(id);
	    if (creator == null) {
	        throw new RuntimeException("Creator not found with id: " + id);
	    }
	    exam.setCreator(creator);
	    return examRepository.save(exam);
	}
		
	 public List<Question>getAllQuestionsByExam(Long idExam) {
		    return examRepository.findAllQuestionsByExamId(idExam);
		  }
		  
	
	 public Question addQuestionToExam(Question question, Long examId) {
		    Optional<Exam> examOptional = examRepository.findById(examId);
		    if (!examOptional.isPresent()) {
		        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Examen non trouvé");
		    }
		    
		 Exam exam = examOptional.get();
		    question.setExam(exam);
		    return questionRepository.save(question);
		}

	
	

}
