package app.controllerTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import app.controller.CandidatoController;
import app.entity.Candidato;
import app.repository.CandidatoRepository;

@SpringBootTest
public class CandidatoControllerTest {

	@Autowired
	CandidatoController candidatoController;

	@MockBean
	CandidatoRepository candidatoRepository;

	@BeforeEach
	void setUp() {

		Candidato candidatoPrefeito = new Candidato(1L, "Benício Viella", "", "48", 1, Candidato.Status.ATIVO, null);
		
		Candidato candidatoVereador = new Candidato(2L, "Giulia Santori", "580.890.910-40", "48488", 2, Candidato.Status.ATIVO, null);
		
		Candidato candidatoInativo = new Candidato(3L, "Zirlanda Lacerda", "580.890.910-40", "48488", 2, Candidato.Status.INATIVO, null);

		Optional<Candidato> optionalPrefeito = Optional.of(candidatoPrefeito);
		
		Optional<Candidato> optionalInativo = Optional.of(candidatoInativo);
		
		List<Candidato> candidatos = new ArrayList<>();
		candidatos.add(candidatoVereador);
		candidatos.add(candidatoPrefeito);
		
		List<Candidato> prefeitosAtivos = new ArrayList<>();
		prefeitosAtivos.add(candidatoPrefeito);
		
		List<Candidato> vereadoresAtivos = new ArrayList<>();
		vereadoresAtivos.add(candidatoVereador);
		
		
		
		Mockito.when(candidatoRepository.findById(1L)).thenReturn(optionalPrefeito);
		
		Mockito.when(candidatoRepository.findById(10L)).thenReturn(Optional.ofNullable(null));

		Mockito.when(candidatoRepository.findById(3L)).thenReturn(optionalInativo);
		
		Mockito.when(candidatoRepository.findByStatus(Candidato.Status.ATIVO)).thenReturn(candidatos);
		
		Mockito.when(candidatoRepository.findByFuncaoAndStatus(1, Candidato.Status.ATIVO)).thenReturn(prefeitosAtivos);
		
		Mockito.when(candidatoRepository.findByFuncaoAndStatus(2, Candidato.Status.ATIVO)).thenReturn(vereadoresAtivos);
		
		doNothing().when(candidatoRepository).deleteById(1L);
		
	}

	@Test
	@DisplayName("Salvar um candidato")
	void cenarioSaveCandidato() {

		Candidato candidato = new Candidato(1L, "Benício Viella", "484.162.980-76", "48", 1, Candidato.Status.ATIVO,
				null);

		Mockito.when(candidatoRepository.save(Mockito.any())).thenReturn(candidato);

		ResponseEntity<String> retorno = this.candidatoController.cadastrarCandidato(candidato);

		String message = retorno.getBody();

		assertEquals("Candidato salvo com sucesso", message);
		assertEquals(HttpStatus.CREATED, retorno.getStatusCode());

	}

	@Test
	@DisplayName("Salvar um candidato nulo - Bad Request")
	void cenarioSaveCandidatoNulo() {

		Candidato candidato = null;

		Mockito.when(candidatoRepository.save(null)).thenReturn(candidato);

		ResponseEntity<String> retorno = this.candidatoController.cadastrarCandidato(candidato);

		assertEquals(HttpStatus.BAD_REQUEST, retorno.getStatusCode());

	}

	@Test
	@DisplayName("Salvar um candidato - Validação de função")
	void cenarioSaveCandidatoFuncaoNull() {

		Candidato candidato = new Candidato(1L, "Benício Viella", null, "48", null, Candidato.Status.ATIVO, null);

		Mockito.when(candidatoRepository.save(Mockito.any())).thenReturn(candidato);

		assertThrows(Exception.class, () -> {
			this.candidatoController.cadastrarCandidato(candidato);
		});

	}

	@Test
	@DisplayName("Erro ao salvar um candidato - Validação de cpf")
	void cenarioSaveCandidatoError() {

		Candidato candidato = new Candidato(1L, "Benício Viella", "", "48", 1, Candidato.Status.ATIVO, null);

		Mockito.when(candidatoRepository.save(Mockito.any())).thenReturn(candidato);

		assertThrows(Exception.class, () -> {
			this.candidatoController.cadastrarCandidato(candidato);
		});

	}

	@Test
	@DisplayName("Atualizar um candidato")
	void cenarioUpdateCandidato() {

		Candidato candidato = new Candidato(1L, "Benício Viella dos Santos", "484.162.980-76", "48", 1,
				Candidato.Status.ATIVO, null);

		ResponseEntity<String> retorno = this.candidatoController.update(candidato, 1);

		String message = retorno.getBody();

		assertEquals("Candidato atualizado com sucesso", message);
		assertEquals(HttpStatus.OK, retorno.getStatusCode());

	}

	@Test
	@DisplayName("Update candidato não encontrado - Bad Request")
	void cenarioUpdateCandidatoNotFind() {

		Candidato candidato = new Candidato(10L, "Benício Viella dos Santos", "484.162.980-76", "48", 1,
				Candidato.Status.ATIVO, null);

		ResponseEntity<String> retorno = this.candidatoController.update(candidato, 10);

		assertEquals(HttpStatus.BAD_REQUEST, retorno.getStatusCode());

	}

	@Test
	@DisplayName("Update candidato inativo - Bad Request")
	void cenarioUpdateCandidatoInativo() {
		
		Candidato candidato = new Candidato(3L, "Giulia Santori", "580.890.910-40", "48488", 2,
				Candidato.Status.INATIVO, null);
		
		ResponseEntity<String> retorno = this.candidatoController.update(candidato, 3);
		
		assertEquals(HttpStatus.BAD_REQUEST, retorno.getStatusCode());
		
	}
	
	@Test
	@DisplayName("Find Candidato By Id")
	void cenarioFindById() {

		ResponseEntity<Candidato> retorno = this.candidatoController.findById(1);

		assertEquals(HttpStatus.OK, retorno.getStatusCode());

	}
	
	@Test
	@DisplayName("findById candidato não encontrado - Bad Request")
	void cenarioFindByIdNotFind() {
		
		ResponseEntity<Candidato> retorno = this.candidatoController.findById(0);
		
		assertEquals(HttpStatus.BAD_REQUEST, retorno.getStatusCode());
		
	}
	
	@Test
	@DisplayName("Find all vereadores ativos")
	void cenarioFindAllCandidatos() {
		
		ResponseEntity<List<Candidato>> retorno = this.candidatoController.findAll();
		
		List<Candidato> candidatos = retorno.getBody();
		
		assertEquals(HttpStatus.OK, retorno.getStatusCode());
		assertEquals(2, candidatos.size());
		
	}
	
	@Test
	@DisplayName("Find prefeitos ativos")
	void cenarioFindPrefeitos() {
		
		ResponseEntity<List<Candidato>> retorno = this.candidatoController.prefeitosAtivos();
		
		List<Candidato> candidatos = retorno.getBody();
		
		assertEquals(HttpStatus.OK, retorno.getStatusCode());
		assertEquals(1, candidatos.size());
		
	}
	
	@Test
	@DisplayName("Find vereadores ativos")
	void cenarioFindVereadores() {
		
		ResponseEntity<List<Candidato>> retorno = this.candidatoController.vereadoresAtivos();
		
		List<Candidato> candidatos = retorno.getBody();
		
		assertEquals(HttpStatus.OK, retorno.getStatusCode());
		assertEquals(1, candidatos.size());
		
	}
	
	@Test
	@DisplayName("Delete do candidato")
	void cenarioDeleteCandidato() {
		
		ResponseEntity<String> retorno = this.candidatoController.delete(1);

		assertEquals("Candidato desativado com sucesso", retorno.getBody());
		assertEquals(HttpStatus.OK, retorno.getStatusCode());
		
	}
	
	
	@Test
	@DisplayName("Delete do candidato - Bad Request")
	void cenarioDeleteCandidatoError() {
		
		ResponseEntity<String> retorno = this.candidatoController.delete(0);
		
		assertEquals(HttpStatus.BAD_REQUEST, retorno.getStatusCode());
		
	}
	
	
}
