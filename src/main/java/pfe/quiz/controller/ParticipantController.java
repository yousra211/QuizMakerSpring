package pfe.quiz.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pfe.quiz.Repository.ParticipantRepository;
import pfe.quiz.model.Answer;
import pfe.quiz.model.Creator;
import pfe.quiz.model.Participant;
import pfe.quiz.model.Question;
import pfe.quiz.service.ParticipantService;
@RestController
@RequestMapping

public class ParticipantController {

	@Autowired ParticipantService participantService;
	@Autowired ParticipantRepository participantRepository;
	
	
	@GetMapping ("participants")
	public List<Participant>getAllParticipants(){
		return participantService.getAllParticipants();
	}
	@PostMapping("participants")
	public Participant addParticipant(@RequestBody Participant participant) {
		return participantService.addParticipant(participant);
	}
	@GetMapping("participants/{id}")
	public Optional<Participant> getParticipantById(@PathVariable Long id) {
		return participantService.getParticipantById(id);
	}
	@DeleteMapping("participants/{id}")
		public void deleteParticipantById(@PathVariable Long id) {
		participantService.deleteParticipantById(id);
		}
	
	 @PostMapping("/participants/{id}/answers")
	 public List<Answer> addAnswersToParticipant( 
	                                         @RequestBody List<Answer> answers,  
	                                         @PathVariable Long id) {
	    
	     return participantService.addAnswersToParticipant( answers, id);
	 }
	 @PutMapping("/participants/{id}/score")
	 public ResponseEntity<?> updateScore(@PathVariable Long id, @RequestBody Double totalScore) {
	     Optional<Participant> optional = participantRepository.findById(id);
	     if (optional.isPresent()) {
	         Participant p = optional.get();
	         p.setTotalScore(totalScore);
	         participantRepository.save(p);
	         return ResponseEntity.ok().build(); // ✅ pas de boucle infinie ici
	     } else {
	         return ResponseEntity.notFound().build();
	     }
	 

	 }

}
