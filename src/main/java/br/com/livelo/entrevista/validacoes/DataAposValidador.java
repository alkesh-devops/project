package br.com.livelo.entrevista.validacoes;

import java.time.LocalDate;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import br.com.livelo.entrevista.validacoes.anotacoes.DataApos;

/**
 * 
 * @author marcelo
 *
 */
public class DataAposValidador implements ConstraintValidator<DataApos, LocalDate> {

	private DataApos constraintAnnotation;

	@Override
	public void initialize(DataApos constraintAnnotation) {
		this.constraintAnnotation = constraintAnnotation;
	}

	@Override
	public boolean isValid(LocalDate value, ConstraintValidatorContext context) {

		final LocalDate min = LocalDate.parse(constraintAnnotation.menorData());
		return value == null || value.isAfter(min);
	}
}