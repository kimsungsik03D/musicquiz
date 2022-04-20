package service.domain.jpa;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SongRepository extends JpaRepository<Song, Long> {

	
	@Query(value="SELECT DISTINCT s.song_num FROM song s WHERE year between ?1 AND ?2", nativeQuery = true)
	List<Integer> getSongId(int toYear, int fromYear);
	
	@Query(value="SELECT * FROM song s WHERE song_num =?1 AND file_num =?2", nativeQuery = true)
	Optional<Song> getSongInfo(int songNum, int random);
}
