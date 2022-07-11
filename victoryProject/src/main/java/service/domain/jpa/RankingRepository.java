package service.domain.jpa;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;



public interface RankingRepository extends JpaRepository<Ranking, Long>{

    @Query(nativeQuery = true, value= "SELECT row, username,score,cleartime FROM ranklist")
    List<Map<String, Object>> findRankList();
    //List<Map<String, Object>> findRankList();
    
}
