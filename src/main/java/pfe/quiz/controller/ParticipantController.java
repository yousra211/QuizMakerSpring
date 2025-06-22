package pfe.quiz.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
import pfe.quiz.service.AnswerService;
import pfe.quiz.service.ParticipantService;
import pfe.quiz.service.QuestionService;
@RestController
@RequestMapping("/participants")

public class ParticipantController {

	@Autowired ParticipantService participantService;
	@Autowired AnswerService answerService ;
	@Autowired QuestionService questionService ;
	@Autowired ParticipantRepository participantRepository;
	
	
	@GetMapping 
	public List<Participant>getAllParticipants(){
		return participantService.getAllParticipants();
	}
	@PostMapping
	public Participant addParticipant(@RequestBody Participant participant) {
		return participantService.addParticipant(participant);
	}
	@GetMapping("/{id}")
	public Optional<Participant> getParticipantById(@PathVariable Long id) {
		return participantService.getParticipantById(id);
	}
	@DeleteMapping("/{id}")
		public void deleteParticipantById(@PathVariable Long id) {
		participantService.deleteParticipantById(id);
		}
	
	@PostMapping("/{participantId}/answers")
	public ResponseEntity<List<Answer>> addAnswersForParticipant(
	        @PathVariable Long participantId,
	        @RequestBody List<Map<String, Object>> answersData) {

	    try {
	        System.out.println("🚀 ===== DÉBUT SAUVEGARDE RÉPONSES =====");
	        System.out.println("📋 Participant ID: " + participantId);
	        System.out.println("📨 Nombre de réponses reçues: " + answersData.size());
	        
	        // Debug détaillé des données reçues
	        for (int i = 0; i < answersData.size(); i++) {
	            Map<String, Object> data = answersData.get(i);
	            System.out.println("📝 Réponse " + i + ": " + data);
	        }

	        // Récupérer le participant
	        Optional<Participant> participantOpt = participantService.findById(participantId);
	        if (!participantOpt.isPresent()) {
	            System.out.println("❌ Participant non trouvé: " + participantId);
	            return ResponseEntity.notFound().build();
	        }
	        Participant participant = participantOpt.get();
	        System.out.println("✅ Participant trouvé: " + participant.getId());

	        List<Answer> answersToSave = new ArrayList<>();

	        for (int i = 0; i < answersData.size(); i++) {
	            Map<String, Object> answerData = answersData.get(i);
	            System.out.println("\n🔍 === TRAITEMENT RÉPONSE " + i + " ===");
	            
	            // Extraire questionId avec debug
	            Long questionId = extractQuestionId(answerData.get("questionId"));
	            System.out.println("🎯 Question ID extrait: " + questionId);
	            
	            if (questionId == null) {
	                System.out.println("⚠️ Question ID null - réponse ignorée");
	                continue;
	            }

	            // Récupérer la question avec debug
	            Optional<Question> questionOpt = questionService.findById(questionId);
	            if (!questionOpt.isPresent()) {
	                System.out.println("❌ Question non trouvée: " + questionId);
	                continue;
	            }
	            Question question = questionOpt.get();
	            System.out.println("✅ Question trouvée: " + question.getId() + " - " + question.getText());

	            // Créer l'Answer avec debug
	            Answer answer = new Answer();
	            String text = (String) answerData.get("text");
	            Boolean isCorrect = (Boolean) answerData.get("isCorrect");
	            
	            answer.setText(text);
	            answer.setIsCorrect(isCorrect);
	            answer.setQuestion(question);
	            answer.setParticipant(participant);
	            
	            System.out.println("📝 Answer créée:");
	            System.out.println("   - Text: " + text);
	            System.out.println("   - IsCorrect: " + isCorrect);
	            System.out.println("   - Question ID: " + question.getId());
	            System.out.println("   - Participant ID: " + participant.getId());

	            answersToSave.add(answer);
	            System.out.println("✅ Answer ajoutée à la liste (total: " + answersToSave.size() + ")");
	        }

	        System.out.println("\n📊 RÉSUMÉ AVANT SAUVEGARDE:");
	        System.out.println("   - Réponses reçues: " + answersData.size());
	        System.out.println("   - Réponses à sauvegarder: " + answersToSave.size());

	        if (answersToSave.isEmpty()) {
	            System.out.println("❌ Aucune réponse valide à sauvegarder");
	            return ResponseEntity.badRequest().build();
	        }

	        // 🔍 VÉRIFICATION AVANT SAUVEGARDE - Compter les answers existantes
	        System.out.println("\n🔍 ÉTAT AVANT SAUVEGARDE:");
	        long countBefore = answerService.countByParticipantId(participantId);
	        System.out.println("   - Answers existantes pour ce participant: " + countBefore);

	        // Sauvegarder avec debug
	        System.out.println("\n💾 SAUVEGARDE EN COURS...");
	        List<Answer> savedAnswers = answerService.saveAll(answersToSave);
	        
	        System.out.println("✅ SAUVEGARDE TERMINÉE:");
	        System.out.println("   - Réponses sauvegardées: " + savedAnswers.size());
	        
	        // 🔍 VÉRIFICATION APRÈS SAUVEGARDE
	        System.out.println("\n🔍 ÉTAT APRÈS SAUVEGARDE:");
	        long countAfter = answerService.countByParticipantId(participantId);
	        System.out.println("   - Answers totales pour ce participant: " + countAfter);
	        System.out.println("   - Différence: +" + (countAfter - countBefore));
	        
	        // Debug des réponses sauvegardées
	        for (int i = 0; i < savedAnswers.size(); i++) {
	            Answer saved = savedAnswers.get(i);
	            System.out.println("   📝 Saved " + i + ": ID=" + saved.getId() + 
	                             ", Text=" + saved.getText() + 
	                             ", QuestionID=" + (saved.getQuestion() != null ? saved.getQuestion().getId() : "null") +
	                             ", ParticipantID=" + (saved.getParticipant() != null ? saved.getParticipant().getId() : "null"));
	        }
	        
	        // 🔍 VÉRIFICATION ULTIME - Récupérer depuis la base
	        System.out.println("\n🔍 VÉRIFICATION ULTIME - Lecture depuis la base:");
	        List<Answer> answersFromDb = answerService.findByParticipantId(participantId);
	        System.out.println("   - Answers trouvées en base pour ce participant: " + answersFromDb.size());
	        
	        // Afficher les 3 dernières pour vérifier
	        int start = Math.max(0, answersFromDb.size() - 3);
	        for (int i = start; i < answersFromDb.size(); i++) {
	            Answer fromDb = answersFromDb.get(i);
	            System.out.println("   🗃️ FromDB " + i + ": ID=" + fromDb.getId() + 
	                             ", Text=" + fromDb.getText() + 
	                             ", QuestionID=" + (fromDb.getQuestion() != null ? fromDb.getQuestion().getId() : "null"));
	        }
	        
	        System.out.println("🎯 ===== FIN SAUVEGARDE RÉPONSES =====\n");
	        
	        return ResponseEntity.ok(savedAnswers);

	    } catch (Exception e) {
	        System.out.println("💥 ERREUR DANS addAnswersForParticipant:");
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	    }
	}

	// 🔍 AJOUTEZ AUSSI CETTE MÉTHODE DE DEBUG pour extractQuestionId
	private Long extractQuestionId(Object questionIdObj) {
	    System.out.println("🔍 extractQuestionId - Input: " + questionIdObj + " (Type: " + (questionIdObj != null ? questionIdObj.getClass().getSimpleName() : "null") + ")");
	    
	    if (questionIdObj == null) {
	        System.out.println("❌ questionId est null");
	        return null;
	    }
	    
	    try {
	        if (questionIdObj instanceof Number) {
	            Long result = ((Number) questionIdObj).longValue();
	            System.out.println("✅ questionId extrait (Number): " + result);
	            return result;
	        } else if (questionIdObj instanceof String) {
	            Long result = Long.parseLong((String) questionIdObj);
	            System.out.println("✅ questionId extrait (String): " + result);
	            return result;
	        } else {
	            System.out.println("❌ Type non supporté pour questionId: " + questionIdObj.getClass());
	            return null;
	        }
	    } catch (Exception e) {
	        System.out.println("❌ Erreur lors de l'extraction de questionId: " + e.getMessage());
	        return null;
	    }
	}

	
	@PutMapping("/{id}/score")
	public ResponseEntity<Participant> updateScore(@PathVariable Long id, @RequestBody Map<String, Double> scoreData) {
	    Double totalScore = scoreData.get("totalScore");
	    Participant participant = participantService.updateScore(id, totalScore);
	    return ResponseEntity.ok(participant);
	}

}
