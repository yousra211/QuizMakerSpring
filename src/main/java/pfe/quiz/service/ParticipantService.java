package pfe.quiz.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import pfe.quiz.Repository.AnswerRepository;
import pfe.quiz.Repository.ParticipantRepository;
import pfe.quiz.model.Answer;
import pfe.quiz.model.Creator;
import pfe.quiz.model.Exam;
import pfe.quiz.model.Participant;
import pfe.quiz.model.Question;

@Service
public class ParticipantService {
	@Autowired ParticipantRepository participantRepository;
	@Autowired AnswerRepository answerRepository;
	@Autowired AnswerService answerService;

	
	public Optional<Participant> findById(Long id){
		return participantRepository.findById(id);
	}
	
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
	// ParticipantService.java
	public Participant updateScore(Long participantId, Double totalScore) {
	    Participant participant = participantRepository.findById(participantId)
	        .orElseThrow(() -> new RuntimeException("Participant not found"));
	    participant.setTotalScore(totalScore);
	    return participantRepository.save(participant);
	}
	/*public void updateTotalScore(Long participantId) {
	    try {
	        System.out.println("🔢 === CALCUL DU SCORE TOTAL ===");
	        System.out.println("📋 Participant ID: " + participantId);
	        
	        // Calculer le score total
	        Double totalScore = answerService.calculateTotalScore(participantId);
	        System.out.println("📊 Score calculé: " + totalScore);
	        
	        // Récupérer et mettre à jour le participant
	        Optional<Participant> participantOpt = findById(participantId);
	        if (participantOpt.isPresent()) {
	            Participant participant = participantOpt.get();
	            participant.setTotalScore(totalScore);
	            participantRepository.save(participant);
	            System.out.println("✅ Score mis à jour pour le participant " + participantId + ": " + totalScore);
	        } else {
	            System.out.println("❌ Participant non trouvé pour mise à jour du score");
	        }
	        
	        System.out.println("🎯 === FIN CALCUL SCORE ===");
	    } catch (Exception e) {
	        System.out.println("💥 Erreur lors du calcul du score:");
	        e.printStackTrace();
	    }
	}
*/
}
