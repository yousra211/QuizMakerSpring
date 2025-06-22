package pfe.quiz.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pfe.quiz.Repository.AnswerRepository;
import pfe.quiz.model.Answer;

@Service
public class AnswerService {

	@Autowired AnswerRepository answerRepository;

	public List<Answer>getAllAnswers(){
		return answerRepository.findAll();
	}

	public Answer addAnswer(Answer answer) {
		return answerRepository.save(answer);
	}

	public Optional<Answer> getAnswerById(Long id){
		return answerRepository.findById(id);
	}
	
	public void deleteAnswerById(Long id){
		answerRepository.deleteById(id);
	}

	 public List<Answer> saveAll(List<Answer> answers) {
        return answerRepository.saveAll(answers);
    }
	 // 🔍 NOUVELLES MÉTHODES POUR DIAGNOSTIC
	    public long countByParticipantId(Long participantId) {
	        return answerRepository.countByParticipantId(participantId);
	    }
	    
	    public List<Answer> findByParticipantId(Long participantId) {
	        return answerRepository.findByParticipantId(participantId);
	    }
	 //public Double calculateTotalScore(Long participantId) {
		//    return answerRepository.calculateTotalScore(participantId);
		//}
}
