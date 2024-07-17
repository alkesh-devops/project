/**
 * 
 */
package br.com.livelo.entrevista.controladores;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoUnit;

import org.apache.http.HttpStatus;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.web.context.WebApplicationContext;

import br.com.livelo.entrevista.EntrevistaLiveloApplication;
import br.com.livelo.entrevista.dominios.SexoEnum;
import br.com.livelo.entrevista.dtos.CidadeDTO;
import br.com.livelo.entrevista.dtos.ClienteDTO;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import io.restassured.module.mockmvc.response.MockMvcResponse;

/**
 * @author marcelo
 *
 */
@Sql(scripts = {"/scripts/TB_CIDADE.sql", "/scripts/TB_CLIENTE.sql"}, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = {"/scripts/TB_CLIENTE_CLEAR.sql", "/scripts/TB_CIDADE_CLEAR.sql"}, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(classes = EntrevistaLiveloApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = EntrevistaLiveloApplication.class)
@ActiveProfiles("test")
public class ClienteControladorTest {

	@Autowired
	private WebApplicationContext context;

	@LocalServerPort
	private int port;

	@Order(1)
	@Test
	public void consultarTodosClientesPaginadoErroQueryParamNome() {

		MockMvcResponse response = RestAssuredMockMvc.given().webAppContextSetup(context)
				.contentType("application/json").get("/clientes");

		response.then()
			.statusCode(is(HttpStatus.SC_BAD_REQUEST))
			.body(containsString("erros"))
			.body("erros", hasSize(1))
			.body("erros", everyItem(hasKey("codigo")))
			.body("erros", everyItem(hasKey("mensagem")))
			.body("erros[0].codigo", equalTo("MSGERRO006"))
			.body("erros[0].mensagem", equalTo("Required String parameter 'nome' is not present"));
	}
	
	@Order(2)
	@Test
	public void consultarTodosClientesPaginado() {
		
		LocalDate dataNascimento = LocalDate.of(1984, 5, 11);
		
		Integer idade = Period.between(dataNascimento, LocalDate.now()).getYears();

		MockMvcResponse response = RestAssuredMockMvc.given().webAppContextSetup(context)
				.contentType("application/json").get("/clientes?nome=Marcelo");

		response.then()
			.statusCode(is(HttpStatus.SC_OK))
			.body(containsString("content"))
			.body("content", hasSize(3))
			.body(containsString("pageable"))
			.body("number", equalTo(0))
			.body("totalPages", equalTo(1))
			.body("size", equalTo(20))
			.body("totalElements", equalTo(3))
			.body("content[2]", hasKey("id"))
			.body("content[2]", hasKey("nome"))
			.body("content[2]", hasKey("sexo"))
			.body("content[2]", hasKey("idade"))
			.body("content[2]", hasKey("dataNascimento"))
			.body("content[2].id", equalTo(3))
			.body("content[2].nome", equalTo("João Marcelo"))
			.body("content[2].sexo", equalTo("M"))
			.body("content[2].idade", equalTo(idade))
			.body("content[2].dataNascimento", equalTo("1984-05-11"));
	}
	
	@Order(3)
	@Test
	public void salvarSucesso() {
		
		LocalDate dataAtual = LocalDate.now();
		// data de nascimento 30 anos 
		LocalDate idade = dataAtual.minus(30, ChronoUnit.YEARS);
		
		DateTimeFormatter formatter = new DateTimeFormatterBuilder()
		        .appendPattern("yyyy-MM-dd").toFormatter();
		
		CidadeDTO cidade = new CidadeDTO();
		cidade.setId(20);
		
		ClienteDTO dto = new ClienteDTO();
		dto.setNome("João de Deus");
		dto.setDataNascimento(idade);
		dto.setSexo(SexoEnum.M);
		dto.setCidade(cidade);

		MockMvcResponse response = RestAssuredMockMvc.given().webAppContextSetup(context)
				.contentType("application/json")
				.body(dto)
				.post("/clientes");

		response.then()
			.statusCode(is(HttpStatus.SC_CREATED))
			.body(containsString("id"))
			.body(containsString("nome"))
			.body(containsString("sexo"))
			.body(containsString("idade"))
			.body(containsString("dataNascimento"))
			.body(containsString("cidade"))
			.body(containsString("_links"))
			.body("nome", equalTo("João de Deus"))
			.body("sexo", equalTo("M"))
			.body("idade", equalTo(30))
			.body("dataNascimento", equalTo(idade.format(formatter)))
			.body("_links", hasKey("self"));
	}
	
	@Test
	public void salvarErro() {
		
		ClienteDTO dto = new ClienteDTO();

		MockMvcResponse response = RestAssuredMockMvc.given().webAppContextSetup(context)
				.contentType("application/json")
				.body(dto)
				.post("/clientes");

		response.then()
			.statusCode(is(HttpStatus.SC_BAD_REQUEST))
			.body(containsString("erros"))
			.body("erros", hasSize(3))
			.body("erros", everyItem(hasKey("codigo")))
			.body("erros", everyItem(hasKey("mensagem")))
			.body("erros[0].codigo", equalTo("MSGERRO008"));
	}

	@Test
	public void consultaPorId() {
		
		LocalDate dataNascimento = LocalDate.of(1983, 5, 11);
		
		Integer idade = Period.between(dataNascimento, LocalDate.now()).getYears();

		MockMvcResponse response = RestAssuredMockMvc.given().webAppContextSetup(context)
				.contentType("application/json").get("/clientes/2");

		response.then()
			.statusCode(is(HttpStatus.SC_OK))
			.body(containsString("id"))
			.body(containsString("nome"))
			.body(containsString("sexo"))
			.body(containsString("idade"))
			.body(containsString("dataNascimento"))
			.body(containsString("cidade"))
			.body(containsString("_links"))
			.body("nome", equalTo("Marcelo Nogueira"))
			.body("dataNascimento", equalTo("1983-05-11"))
			.body("idade", equalTo(idade))
			.body("_links", hasKey("self"));
	}
	
	@Test
	public void consultaPorIdNotFound() throws Exception {

		MockMvcResponse response = RestAssuredMockMvc.given().webAppContextSetup(context)
				.contentType("application/json").get("/clientes/30");

		response.then()
			.statusCode(is(HttpStatus.SC_NOT_FOUND))
			.body("$", hasKey("erros"))
			.body("erros", hasSize(1))
			.body("erros[0]", hasKey("codigo"))
			.body("erros[0]", hasKey("mensagem"))
			.body("erros[0].codigo", equalTo("MSGERRO003"))
			.body("erros[0].mensagem", equalTo("Entidade ou recurso não encontrado"));
	}
	
	@Test
	public void deletarIdNotFound() throws Exception {

		MockMvcResponse response = RestAssuredMockMvc.given().webAppContextSetup(context)
				.contentType("application/json").delete("/clientes/30");

		response.then()
			.statusCode(is(HttpStatus.SC_BAD_REQUEST))
			.body("$", hasKey("erros"))
			.body("erros", hasSize(1))
			.body("erros[0]", hasKey("codigo"))
			.body("erros[0]", hasKey("mensagem"))
			.body("erros[0].codigo", equalTo("MSGERRO002"))
			.body("erros[0].mensagem", equalTo("No class br.com.livelo.entrevista.entidades.Cliente entity with id 30 exists!"));
	}
	
	@Order(4)
	@Test
	public void alterarErroNomeVazio() throws Exception {
		
		ClienteDTO cliente = new ClienteDTO();
		cliente.setNome("");

		MockMvcResponse response = RestAssuredMockMvc.given().webAppContextSetup(context)
				.contentType("application/json").body(cliente).put("/clientes/2");

		response.then()
		.statusCode(is(HttpStatus.SC_BAD_REQUEST))
		.body("$", hasKey("erros"))
		.body("erros", hasSize(1))
		.body("erros[0]", hasKey("codigo"))
		.body("erros[0]", hasKey("mensagem"))
		.body("erros[0].codigo", equalTo("MSGERRO004"));
	}
	
	@Order(5)
	@Test
	public void alterarIdErro() throws Exception {
		
		ClienteDTO cliente = new ClienteDTO();
		cliente.setNome("Erro ID erro");

		MockMvcResponse response = RestAssuredMockMvc.given().webAppContextSetup(context)
				.contentType("application/json").body(cliente).put("/clientes/30");

		response.then()
		.statusCode(is(HttpStatus.SC_NOT_FOUND))
		.body("$", hasKey("erros"))
		.body("erros", hasSize(1))
		.body("erros[0]", hasKey("codigo"))
		.body("erros[0]", hasKey("mensagem"))
		.body("erros[0].codigo", equalTo("MSGERRO003"));
	}
	
	@Order(6)
	@Test
	public void alterarSucesso() throws Exception {
		
		ClienteDTO cliente = new ClienteDTO();
		cliente.setNome("João do Pulo");

		MockMvcResponse response = RestAssuredMockMvc.given().webAppContextSetup(context)
				.contentType("application/json").body(cliente).put("/clientes/2");

		response.then()
		.statusCode(is(HttpStatus.SC_OK))
		.body(containsString("id"))
		.body(containsString("nome"))
		.body(containsString("sexo"))
		.body(containsString("idade"))
		.body(containsString("dataNascimento"))
		.body(containsString("cidade"))
		.body(containsString("_links"))
		.body("nome", equalTo("João do Pulo"))
		.body("dataNascimento", equalTo("1983-05-11"));
	}
	
	
	@Order(7)
	@Test
	public void deletarSucesso() throws Exception {
		
		MockMvcResponse response = RestAssuredMockMvc.given().webAppContextSetup(context)
				.contentType("application/json").delete("/clientes/1");

		response.prettyPrint();
		response.then()
		.statusCode(is(HttpStatus.SC_OK));
	}

}
