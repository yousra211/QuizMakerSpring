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

import pfe.quiz.model.Participant;
import pfe.quiz.service.ParticipantService;
@RestController
@RequestMapping

public class ParticipantController {

	@Autowired ParticipantService participantService;
	
	
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
}
