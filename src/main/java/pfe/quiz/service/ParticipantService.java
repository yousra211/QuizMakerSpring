package pfe.quiz.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pfe.quiz.Repository.AnswerRepository;
import pfe.quiz.Repository.ParticipantRepository;
import pfe.quiz.model.Answer;
import pfe.quiz.model.Exam;
import pfe.quiz.model.Participant;
import pfe.quiz.model.Question;

@Service
public class ParticipantService {
	@Autowired ParticipantRepository participantRepository;
	@Autowired AnswerRepository answerRepository;

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

	public List<Answer> addAnswersToParticipant(List<Answer> answers, Long id) {
		
			    // Récupérer l'exam
			    Participant participant = participantRepository.findById(id)
			        .orElseThrow(() -> new RuntimeException("participant not found with id: " + id));
			    
			    
			    
			    // Associer chaque question à l'examen
			    List<Answer> savedAnswers = new ArrayList<>();
			    for (Answer answer : answers) {
			    	answer.setParticipant(participant);
			        savedAnswers.add(answerRepository.save(answer));
			    }
			    
			    return savedAnswers;
			
	}

}
