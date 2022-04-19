package service.domain.jpa;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.data.annotation.CreatedDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="song")
public class Song {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="uld")
	private int uld ;

	private String singer ;
	
	
	private String title ;
	
	private String genere ;
	
	private int year ;
	
	
	private String fileName ;
	
	private String urlId ;
	
	private int songNum;
	
	private int fileNum;
	
	private String singerHint;
	
	private String titleHint;
	
	
}
