package pfe.quiz.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pfe.quiz.model.Answer;
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
		return examRepository.save(exam);
	}
	
	public Optional<Exam> getExamById(Long id) {
		return examRepository.findById(id);
	}

	public void deleteExamById(Long id) {
		examRepository.deleteById(id);
		
	}

	public List<Exam> getAllExamByCreator(String userNameCreator) {
		return examRepository.findByCreator_Username(userNameCreator);
	}

	public Exam addExamToCreator(String username, Exam exam) {
		exam.setCreator(creatorRepository.findCreatorByUsername(username));
		examRepository.save(exam);
		return exam;
	}
		
	 public List<Question>getAllQuestionsByExam(Long idExam) {
		    return examRepository.findAllQuestionsByExamId(idExam);
		  }
		  
	
	public Question addQuestionToExam (Question question , Long idExam) {
		Optional<Exam> exam=examRepository.findById(idExam);
		question.setExam(exam.get());
		return questionRepository.save(question);
	}

	
	

}
