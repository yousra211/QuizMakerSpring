package pfe.quiz.model;
import lombok.Data;
@Data

public class CreatorResponse {
    private Long id;
    private String fullname;
    private String username;
    private String email;
    private String roles;

    public CreatorResponse(Creator creator) {
        this.id = creator.getId();
        this.fullname = creator.getFullname();
        this.username = creator.getUsername();
        this.email = creator.getEmail();
        this.roles = creator.getRolesString();
    }

    
}

