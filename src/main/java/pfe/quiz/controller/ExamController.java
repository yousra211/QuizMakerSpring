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
	@DeleteMapping("/{id}")
	public void deleteAllExamById (@PathVariable Long id){
		 examService.deleteExamById(id);
	}
	
	 @GetMapping ("questions/{idExam}")
     public List<Question>getAllQuestionsByExam(@PathVariable Long idExam){
      return examService.getAllQuestionsByExam(idExam);
     }
     
	
	 
	 @PostMapping("exam/{idExam}/questions")
	 public Question addQuestionToExam(@RequestBody Question question, 
	                                 @PathVariable Long idExam, 
	                                 Authentication authentication) {
	     Creator creator = (Creator) authentication.getPrincipal();
	     
	     // Récupérer l'exam en gérant l'Optional
	     Exam exam = examService.getExamById(idExam)
	             .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Examen non trouvé"));
	     
	     // Vérifier les permissions
	     if (!exam.getCreator().getId().equals(creator.getId())) {
	         throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Vous n'êtes pas autorisé à modifier cet examen");
	     }
	     
	     return examService.addQuestionToExam(question, idExam);
	 }
}
