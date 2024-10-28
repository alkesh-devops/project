/**
 * 
 */
package br.com.livelo.entrevista;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;

/**
 * @author marcelo
 *
 */
@Configuration
public class JacksonConfiguration {

	/**
	 * Método que define as configurações de serialização e deserialização 
	 * 
	 * @param builder Builder utilizado para as definições das configurações
	 * 
	 * @return {@link ObjectMapper} configurado
	 */
	@Bean
	public ObjectMapper mapper(Jackson2ObjectMapperBuilder builder) {

		ObjectMapper mapper = builder.build();

		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
		mapper.setSerializationInclusion(Include.NON_NULL);
		
		SimpleModule module = new SimpleModule();
		mapper.registerModule(module);
		mapper.registerModule(hibernate5Module());

		return mapper;
	}
	
	/**
	 * Módulo do hibernate que desconsidera properiedades LAZY
	 * 
	 * @return {@link Hibernate5Module}
	 */
	@Bean
    public Hibernate5Module hibernate5Module() {
        Hibernate5Module module = new Hibernate5Module();
        module.disable(Hibernate5Module.Feature.USE_TRANSIENT_ANNOTATION);
        module.enable(Hibernate5Module.Feature.SERIALIZE_IDENTIFIER_FOR_LAZY_NOT_LOADED_OBJECTS);
        return module;
    }
}
