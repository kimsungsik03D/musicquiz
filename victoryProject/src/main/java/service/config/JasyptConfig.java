/*application.properteis의 값들을 암호한 값들은 복호화할때 사용되는 코드
 * 다른 서비스들도 동일한게 구성되어 있음*/

package service.config;

import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class JasyptConfig {
	private static final String ENCRYPT_KEY = System.getenv("webkey");


	@Bean("jasyptStringEncryptor")
	public StringEncryptor stringEncryptor(Environment env) {
		SimpleStringPBEConfig config = new SimpleStringPBEConfig();
         
		config.setPassword(ENCRYPT_KEY);
		config.setAlgorithm("PBEWithMD5AndDES");
		
		config.setPoolSize("1");
		config.setProviderName("SunJCE");
		config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
		config.setStringOutputType("base64");
		
		PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
		encryptor.setConfig(config);
		
		return encryptor;
	}
}