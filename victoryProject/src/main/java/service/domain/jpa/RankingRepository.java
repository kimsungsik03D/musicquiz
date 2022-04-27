package service.domain.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;



public interface RankingRepository extends JpaRepository<Ranking, Long>{

    @Query(nativeQuery = true, value= "SELECT * FROM ranklist")
    List<Ranking> findRankList();
	
}
