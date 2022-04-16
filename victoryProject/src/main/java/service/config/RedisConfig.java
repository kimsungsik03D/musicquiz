package service.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
//세션을 redis에서 관리
//@EnableRedisHttpSession
public class RedisConfig {

	@Value("${spring.redis.host}")
	private String redisHostName;
	
	@Value("${spring.redis.password}")
	private String redisPassword;
	
	@Value("${spring.redis.port}")
	private int redisPort;
	
	
    @Bean
    RedisConnectionFactory  redisConnectionFactory(){

        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration(redisHostName,redisPort);
        redisStandaloneConfiguration.setPassword(RedisPassword.of(redisPassword));
        LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory(redisStandaloneConfiguration);

        return  lettuceConnectionFactory;
    }
    

    

}
