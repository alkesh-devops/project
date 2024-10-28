package br.com.livelo.entrevista.validacoes.anotacoes;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import br.com.livelo.entrevista.validacoes.DataAposValidador;

/**
 * 
 * @author marcelo
 *
 */
@Documented
@Constraint(validatedBy = DataAposValidador.class)
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface DataApos {

	String message() default "MSG001";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	/**
	 * Menor data possível
	 * 
	 * @return A menor data possível
	 */
	String menorData();

}
