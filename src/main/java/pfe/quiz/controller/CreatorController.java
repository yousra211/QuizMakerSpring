package pfe.quiz.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import pfe.quiz.model.Exam;
import pfe.quiz.Repository.CreatorRepository;
import pfe.quiz.model.Creator;
import pfe.quiz.service.ExamService;
import pfe.quiz.service.CreatorService;

@RestController
@CrossOrigin(origins = "*")  
@RequestMapping
public class CreatorController {
	@Autowired CreatorService creatorService;
	@Autowired ExamService examService;
	@Autowired CreatorRepository creatorRepository;
	
	@GetMapping ("creators")
	public List<Creator>getAllCreators(){
		return creatorService.getAllCreators();
	}
	
	@GetMapping("photos/{id}")
	public ResponseEntity<Resource> getImage(@PathVariable String id ){
		String path="src/main/resources/static/photos/"+id+".png";
		FileSystemResource file =new FileSystemResource (path);
		if (!file.exists()) {
			return ResponseEntity.notFound().build();
		
		}
		return ResponseEntity.ok()
				.contentType(MediaType.IMAGE_PNG)
				.body(file);
	}
	/*
	@PostMapping("creators")
	public Creator addCreator(@RequestBody Creator creator) {
	    creator.setPhotoUrl("http://localhost:8080/photos/default.png"); // valeur par défaut
	    creator.setActive(true);
	    creator.setRoles("CREATOR"); // valeur par défaut
	    return creatorService.addCreator(creator);
	}

	*/
	@PostMapping("creators")
	 public Creator addCreator(
	  @RequestParam Long id,
	  @RequestParam String fullname,
	  @RequestParam String username,
	  @RequestParam  String email,
	  @RequestParam  String password,
	  @RequestParam MultipartFile file)
	 throws IllegalStateException, IOException, InterruptedException{
	  String path="src/main/resources/static/photos/"+id+".jpeg";
	  file.transferTo(Path.of(path));
	  String url="http://localhost:8080/photos/"+id;
	  Creator creator= new Creator(id,fullname,username,email,password,url,true,"CREATOR",null);
	   return creatorService.addCreator(creator);
	 }
	
	@GetMapping("creators/{id}")
	public Optional<Creator> getCreatorById(@PathVariable Long id) {
		return creatorService.getCreatorById(id);
	}
	@DeleteMapping("creators/{id}")
		public boolean deleteCreatorById(@PathVariable Long id) {
		String path="src/main/resources/static/photos/"+id+".png";
		File f=new File(path);
		if (f.exists())f.delete();
	creatorService.deleteCreatorById(id);
	
	return !creatorRepository.existsById(id);
}
	
	@PutMapping ("creators")
	
	public Creator updateCreator(@RequestBody Creator creator) {
		return creatorService.updateCreator(creator);
	}
	
	//get all Exam by creator
	@GetMapping("exams/creator")
		public List<Exam> getExamsByCreator(@PathVariable Long id){
		return examService.getExamsByCreator(id);
		}
			
			
	@PostMapping("creator/{id}/exam")
	    public Exam addExamToCreator( Authentication authentication, @RequestBody Exam exam){
		 Creator creator = (Creator) authentication.getPrincipal();
		return examService.addExamToCreator(creator.getId(),exam);
		}


	/*@PostMapping("creators")
	public Creator addCreator(@RequestBody Creator creator) {
		return creatorService.addCreator(creator);
	}
	*/



 /*@DeleteMapping("creators/{id}")
public boolean  deleteCreator(@PathVariable Long id){
creatorRepository.deleteById(id);
return !creatorRepository.existsById(id);

}
*/}


