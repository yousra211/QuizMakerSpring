package pfe.quiz.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pfe.quiz.model.Exam;
import pfe.quiz.model.Question;
import pfe.quiz.service.ExamService;

@RestController
@CrossOrigin(origins = "*")  
@RequestMapping
public class ExamController {
@Autowired ExamService examService;
	

	@GetMapping("exams")
	public List<Exam> getAllExams(){
		return examService.getAllExams();
		
	}
	
	@PostMapping("exams")
	public Exam addExam(@RequestBody Exam exam){
		return examService.addExam(exam);
		
	}
	@GetMapping("exams/{id}")
	public Optional<Exam> getExamById (@PathVariable Long id){
		return examService.getExamById(id);
		
	}
	@DeleteMapping("exams/{id}")
	public void deleteAllExamById (@PathVariable Long id){
		 examService.deleteExamById(id);
	}
	
	 @GetMapping ("questions/{idExam}")
     public List<Question>getAllQuestionsByExam(@PathVariable Long idExam){
      return examService.getAllQuestionsByExam(idExam);
     }
     
	
    @PostMapping("questions/{idExam}")
    public Question addQuestionToExam(@RequestBody Question question,@PathVariable Long idExam) {
    return examService.addQuestionToExam(question,idExam);
   }
    
  
    
  
    
}
