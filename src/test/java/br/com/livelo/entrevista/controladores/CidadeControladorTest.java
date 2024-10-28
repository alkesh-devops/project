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
import br.com.livelo.entrevista.dtos.CidadeDTO;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import io.restassured.module.mockmvc.response.MockMvcResponse;

/**
 * @author marcelo
 *
 */
@Sql(scripts = {"/scripts/TB_CIDADE.sql"}, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = {"/scripts/TB_CIDADE_CLEAR.sql"}, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(classes = EntrevistaLiveloApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = EntrevistaLiveloApplication.class)
@ActiveProfiles("test")
public class CidadeControladorTest {

	@Autowired
	private WebApplicationContext context;

	@LocalServerPort
	private int port;

	@Order(1)
	@Test
	public void consultarTodasCidadesPaginada() {

		MockMvcResponse response = RestAssuredMockMvc.given().webAppContextSetup(context)
				.contentType("application/json").get("/cidades");

		response.then()
			.statusCode(is(HttpStatus.SC_OK))
			.body(containsString("content"))
			.body("content", hasSize(20))
			.body(containsString("pageable"))
			.body("number", equalTo(0))
			.body("totalPages", equalTo(1))
			.body("size", equalTo(20))
			.body("totalElements", equalTo(20));
	}
	
	@Order(2)
	@Test
	public void consultarCidadePorNome() {

		MockMvcResponse response = RestAssuredMockMvc.given().webAppContextSetup(context)
				.contentType("application/json").get("/cidades?nome=Pessoa");

		response.then()
			.statusCode(is(HttpStatus.SC_OK))
			.body(containsString("content"))
			.body("content", hasSize(1))
			.body(containsString("pageable"))
			.body("number", equalTo(0))
			.body("totalPages", equalTo(1))
			.body("size", equalTo(20))
			.body("totalElements", equalTo(1))
			.body("content[0]", hasKey("id"))
			.body("content[0]", hasKey("nome"))
			.body("content[0]", hasKey("estado"))
			.body("content[0].id", equalTo(20))
			.body("content[0].nome", equalTo("João Pessoa"))
			.body("content[0].estado", equalTo("PB"));
	}
	
	@Order(3)
	@Test
	public void consultarCidadePorNomeCacimba() {

		MockMvcResponse response = RestAssuredMockMvc.given().webAppContextSetup(context)
				.contentType("application/json").get("/cidades?nome=Cacimba");

		response.then()
			.statusCode(is(HttpStatus.SC_OK))
			.body(containsString("content"))
			.body("content", hasSize(2))
			.body(containsString("pageable"))
			.body("number", equalTo(0))
			.body("totalPages", equalTo(1))
			.body("size", equalTo(20))
			.body("totalElements", equalTo(2));
	}
	
	@Test
	public void salvarSucesso() {
		
		CidadeDTO dto = new CidadeDTO();
		dto.setNome("Itabaiana");
		dto.setEstado("PB");

		MockMvcResponse response = RestAssuredMockMvc.given().webAppContextSetup(context)
				.contentType("application/json")
				.body(dto)
				.post("/cidades");

		response.then()
			.statusCode(is(HttpStatus.SC_CREATED))
			.body("nome", equalTo("Itabaiana"))
			.body("estado", equalTo("PB"));
	}
	
	@Test
	public void salvarErro() {
		
		CidadeDTO dto = new CidadeDTO();
		dto.setNome("");
		dto.setEstado("");

		MockMvcResponse response = RestAssuredMockMvc.given().webAppContextSetup(context)
				.contentType("application/json")
				.body(dto)
				.post("/cidades");

		response.then()
			.statusCode(is(HttpStatus.SC_BAD_REQUEST))
			.body(containsString("erros"))
			.body("erros", hasSize(2))
			.body("erros", everyItem(hasKey("codigo")))
			.body("erros", everyItem(hasKey("mensagem")))
			.body("erros[0].codigo", equalTo("MSGERRO008"))
			.body("erros[0].mensagem", equalTo("must not be empty"));
	}
}
