/*
 * 저장 타입이 HashMap으로 고정됨
 * */

package service.domain.redis;

import org.springframework.data.repository.CrudRepository;

public interface RedisUserRepository extends CrudRepository<RedisUser, Object> {

}
