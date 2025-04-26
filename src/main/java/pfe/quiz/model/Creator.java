package pfe.quiz.model;

import java.nio.file.attribute.UserDefinedFileAttributeView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor @NoArgsConstructor @Data
@Entity
public class Creator implements UserDetails {

	@Id
	   @GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id ;
	String fullname;
	String username;
	String email ;
	String password;
	 String photoUrl;
	 boolean active;
	 String roles;
	
	@OneToMany(mappedBy = "creator", fetch = FetchType.EAGER)
	@JsonIgnore
	List<Exam> exams;

	
	public String getPassword() {
	    return password;
	}

	
	public List<String> getRoles(){
		return Arrays.asList(roles.split(","));
	}
	public String getRolesString() {
	    return this.roles;
	}

	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<GrantedAuthority> authorities=new ArrayList<>();
		this.getRoles().forEach(role->{
		authorities.add(new SimpleGrantedAuthority(role));
		});
		return authorities;
		}
	@Override
	public boolean isAccountNonExpired() {
	    return active;
	}

	@Override
	public boolean isAccountNonLocked() {
	    return active;
	}

	@Override
	public boolean isCredentialsNonExpired() {
	    return active;
	}

	@Override
	public boolean isEnabled() {
	    return active;
	}

	// Pour compatibilité avec UserDetails
	@Override
	public String getUsername() {
	    return this.email; // Utiliser l'email comme identifiant
	}
	
}

