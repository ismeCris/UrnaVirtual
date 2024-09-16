package app.serviceTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.mockito.Mockito.*;
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

	@Test
	void cenarioFuncaoInvalid() {
		Candidato candidatoInvalido = new Candidato();
		candidatoInvalido.setFuncao(3);

		RuntimeException exception = assertThrows(RuntimeException.class, () -> {
			candidatoService.cadastrarCandidato(candidatoInvalido);
		});

		assertEquals("A função deve ser 1 para prefeito ou 2 para vereador", exception.getMessage());
		verify(candidatoRepository, never()).save(any(Candidato.class));
	}

	@Test
	public void ComFuncaoValidaPrefeito() {
		Candidato candidatoPrefeito = new Candidato();
		candidatoPrefeito.setFuncao(1); // prefeito

		String resultado = candidatoService.cadastrarCandidato(candidatoPrefeito);

		assertEquals("Candidato salvo com sucesso", resultado);
		assertEquals(Candidato.Status.ATIVO, candidatoPrefeito.getStatus());

		verify(candidatoRepository, times(1)).save(candidatoPrefeito);
	}

	@Test
	public void deveCadastrarCandidatoComFuncaoValidaVereador() {
		Candidato candidatoVereador = new Candidato();
		candidatoVereador.setFuncao(2); // Vereador

		String resultado = candidatoService.cadastrarCandidato(candidatoVereador);

		assertEquals("Candidato salvo com sucesso", resultado);
		assertEquals(Candidato.Status.ATIVO, candidatoVereador.getStatus());

		verify(candidatoRepository, times(1)).save(candidatoVereador);
	}
}
