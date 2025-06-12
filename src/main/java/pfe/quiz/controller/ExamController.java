package pfe.quiz.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import pfe.quiz.model.Creator;
import pfe.quiz.model.Exam;
import pfe.quiz.model.Question;
import pfe.quiz.service.ExamService;

@RestController
@CrossOrigin(origins = "*")  
@RequestMapping("/exams")
public class ExamController {
@Autowired ExamService examService;
	

	@GetMapping
	public List<Exam> getAllExams(){
		return examService.getAllExams();
		
	}
	
	@PostMapping
	public Exam addExam(@RequestBody Exam exam){
		return examService.addExam(exam);
		
	}
	@GetMapping("/{id}")
	public ResponseEntity<Exam> getExamById(@PathVariable Long id) {
	    Exam exam = examService.getExamById(id)
	            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Examen non trouvé"));
	    return ResponseEntity.ok(exam);
	}
	@PutMapping 
	public Exam updateExam( @RequestBody Exam exam) {
		return examService.updateExam( exam);
	     
	}
	
	@DeleteMapping("/{id}")
	public void deleteAllExamById (@PathVariable Long id){
		 examService.deleteExamById(id);
	}
	
	 @GetMapping ("/{idExam}/questions")
     public List<Question>getAllQuestionsByExam(@PathVariable Long idExam){
      return examService.getAllQuestionsByExam(idExam);
     }
     
	
	 
	 @PostMapping("/{id}/questions")
	 public List<Question> addQuestionsToExam(Authentication authentication, 
	                                         @RequestBody List<Question> questions,  // ⚠️ LIST, pas Question
	                                         @PathVariable Long id) {
	     Creator creator = (Creator) authentication.getPrincipal();
	     return examService.addQuestionsToExam(creator.getId(), questions, id);
	 }
}