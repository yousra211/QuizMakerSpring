package pfe.quiz.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pfe.quiz.Repository.ParticipantRepository;
import pfe.quiz.model.Participant;

@Service
public class ParticipantService {
	@Autowired ParticipantRepository participantRepository;

	public List<Participant>getAllParticipants(){
		return participantRepository.findAll();
	}

	public Participant addParticipant(Participant participant) {
		return participantRepository.save(participant);
	}

	public Optional<Participant> getParticipantById(Long id){
		return participantRepository.findById(id);
	}
	
	public void deleteParticipantById(Long id){
		participantRepository.deleteById(id);
	}

}
