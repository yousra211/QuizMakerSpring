package pfe.quiz.service;

import java.util.ArrayList;
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
import pfe.quiz.model.Participant;
import pfe.quiz.model.Question;
import pfe.quiz.Repository.ExamRepository;
import pfe.quiz.Repository.QuestionRepository;
import pfe.quiz.Repository.AnswerRepository;
import pfe.quiz.Repository.CreatorRepository;
import pfe.quiz.Repository.ParticipantRepository;


@Service
public class ExamService {

	@Autowired ExamRepository examRepository;
@Autowired CreatorRepository creatorRepository;
@Autowired QuestionRepository questionRepository;
@Autowired AnswerRepository answerRepository;	
@Autowired ParticipantRepository ParticipantRepository;	


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
	
	public Exam updateExam( Exam exam) {
		return examRepository.save(exam);
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
		  
	
	 public List<Question> addQuestionsToExam(Long creatorId, List<Question> questions, Long examId) {
		    // Récupérer l'exam
		    Exam exam = examRepository.findById(examId)
		        .orElseThrow(() -> new RuntimeException("Exam not found with id: " + examId));
		    
		    // Vérifier que l'exam appartient au créateur authentifié
		    if (!exam.getCreator().getId().equals(creatorId)) {
		        throw new RuntimeException("Creator not authorized to modify this exam");
		    }
		    
		    // Associer chaque question à l'examen
		    List<Question> savedQuestions = new ArrayList<>();
		    for (Question question : questions) {
		        question.setExam(exam);
		        savedQuestions.add(questionRepository.save(question));
		    }
		    
		    return savedQuestions;
		}
	 
	 public List<Participant>getResultsForExam(Long examId) {
		    List<Participant>allParticipants=ParticipantRepository.findAll();

		    return allParticipants.stream()
		        .filter(p -> p.getAnswers().stream().anyMatch(a ->
		            a.getQuestion() != null &&
		            a.getQuestion().getExam() != null &&
		            a.getQuestion().getExam().getId().equals(examId)))
		        .peek(p -> {
		            double score = p.getAnswers().stream()
		                .filter(a -> a.getQuestion().getExam().getId().equals(examId))
		                .filter(a -> Boolean.TRUE.equals(a.getIsCorrect()))
		                .mapToDouble(a -> a.getQuestion().getGrade())
		                .sum();
		            p.setTotalScore(score);
		        })
		      .toList();
		}
	
	

}
