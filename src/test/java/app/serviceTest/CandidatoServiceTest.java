package app.serviceTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import app.entity.Candidato;
import app.repository.CandidatoRepository;
import app.service.CandidatoService;

@SpringBootTest
public class CandidatoServiceTest {

	@Autowired
	CandidatoService candidatoService;

	@MockBean
	CandidatoRepository candidatoRepository;

	@BeforeEach
	void setup() {

		Candidato candidatoPrefeito = new Candidato(1L, "Benício Viella", "", "48", 1, Candidato.Status.ATIVO, null);

		Optional<Candidato> optionalPrefeito = Optional.of(candidatoPrefeito);


		Mockito.when(candidatoRepository.findByNumero("48")).thenReturn(optionalPrefeito);

		Mockito.when(candidatoRepository.findByNumero("84")).thenReturn(Optional.ofNullable(null));

	}

	@Test
	@DisplayName("Find Candidato By Número")
	void cenarioFindById() {

		Candidato candidato = this.candidatoService.findByNumero("48");

		assertEquals("48", candidato.getNumero());

	}

	@Test
	@DisplayName("findByNumero candidato não encontrado - Bad Request")
	void cenarioFindByIdNotFind() {

		assertThrows(RuntimeException.class, () -> {
			this.candidatoService.findByNumero("84");
		});

	}

}
