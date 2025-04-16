package pfe.quiz.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pfe.quiz.model.Answer;
import pfe.quiz.service.AnswerService;

@RestController
@RequestMapping
public class AnswerController {

@Autowired AnswerService answerService;
	
	
	@GetMapping ("answers")
	public List<Answer>getAllAnswers(){
		return answerService.getAllAnswers();
	}
	@PostMapping("answers")
	public Answer addAnswer(@RequestBody Answer answer) {
		return answerService.addAnswer(answer);
	}
	@GetMapping("answers/{id}")
	public Optional<Answer> getAnswerById(@PathVariable Long id) {
		return answerService.getAnswerById(id);
	}
	@DeleteMapping("answers/{id}")
		public void deleteAnswerById(@PathVariable Long id) {
		answerService.deleteAnswerById(id);
		}
}
