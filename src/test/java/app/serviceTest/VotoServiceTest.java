package app.serviceTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import app.entity.Candidato;
import app.entity.Eleitor;
import app.entity.Voto;
import app.service.EleitorService;
import app.service.VotoService;
import net.bytebuddy.asm.Advice.Local;

@SpringBootTest
public class VotoServiceTest {

	@Autowired
	VotoService votoService;

	@MockBean
	EleitorService eleitorService;

	void setUp() {
		doNothing().when(eleitorService).update(Mockito.any(), Mockito.anyLong());
	}

	@Test
	@DisplayName("Verificando um eleitor apto")
	void cenarioEleitorApto() {
		Eleitor eleitor = new Eleitor(1, "Mikael Boreski", "198.927.837-28", "Biólogo", "(11) 93784- 7834", null,
				"mikaelbiologo@gmail.com", Eleitor.Status.APTO);

		boolean isApto = votoService.eleitorApto(eleitor);

		assertEquals(true, isApto);
	}

	@Test
	@DisplayName("Verificando um eleitor pendente")
	void cenarioEleitorPendente() {
		Eleitor eleitor = new Eleitor(2, "Paola Álvares", null, "Chef de cozinha", "(41) 98723- 6372", null, null,
				Eleitor.Status.PENDENTE);

		assertThrows(Exception.class, () -> {
			votoService.eleitorApto(eleitor);
		});

	}

	@Test
	@DisplayName("Verificando um eleitor bloqueado")
	void cenarioEleitorBloqueado() {
		Eleitor eleitor = new Eleitor(2, "Paola Álvares", "845.383.593-49", "Chef de cozinha", "(41) 98723- 6372", null,
				"alvspaola@hotmail.com", Eleitor.Status.BLOQUEADO);

		boolean isApto = votoService.eleitorApto(eleitor);

		assertEquals(false, isApto);

	}

	@Test
	@DisplayName("Verificando candidatos válidos")
	void cenarioVerificaCandidato() {

		Candidato candidatoPrefeito = new Candidato(1L, "Benício Viella", "", "48", 1, Candidato.Status.ATIVO, null);

		Candidato candidatoVereador = new Candidato(2L, "Giulia Santori", "", "48488", 2, Candidato.Status.ATIVO, null);

		Voto voto = new Voto(1, null, null, candidatoPrefeito, candidatoVereador);

		boolean areValidos = votoService.verificarCandidatos(voto);

		assertEquals(true, areValidos);
	}

	@Test
	@DisplayName("Verificando candidatos inválidos - Canditado Nulo")
	void cenarioVerificaCandidatoNulo() {

		Candidato candidatoPrefeito = new Candidato(1L, "Benício Viella", "", "48", 1, Candidato.Status.ATIVO, null);

		Candidato candidatoVereador = null;

		Voto voto = new Voto(1, null, null, candidatoPrefeito, candidatoVereador);

		Exception exception = assertThrows(Exception.class, () -> {
			votoService.verificarCandidatos(voto);
		});
		assertEquals("Os candidatos para prefeito e vereador devem ser selecionados", exception.getMessage());
	}

	@Test
	@DisplayName("Verificando canditado a prefeito com função nula")
	void cenarioFuncaoNulaPrefeito() {

		Candidato candidatoPrefeito = new Candidato(1L, "Benício Viella", "", "48", null, Candidato.Status.ATIVO, null);

		Candidato candidatoVereador = new Candidato(2L, "Giulia Santori", "", "48488", 2, Candidato.Status.ATIVO, null);

		Voto voto = new Voto(1, null, null, candidatoPrefeito, candidatoVereador);

		Exception exception = assertThrows(Exception.class, () -> {
			votoService.verificarCandidatos(voto);
		});

		assertEquals("A função do candidato a prefeito é nula", exception.getMessage());
	}

	@Test
	@DisplayName("Verificando canditado a vereador com função nula")
	void cenarioFuncaoNulaVereador() {

		Candidato candidatoPrefeito = new Candidato(1L, "Benício Viella", "", "48", 1, Candidato.Status.ATIVO, null);

		Candidato candidatoVereador = new Candidato(2L, "Giulia Santori", "", "48488", null, Candidato.Status.ATIVO,
				null);

		Voto voto = new Voto(1, null, null, candidatoPrefeito, candidatoVereador);

		Exception exception = assertThrows(Exception.class, () -> {
			votoService.verificarCandidatos(voto);
		});

		assertEquals("A função do candidato a vereador é nula", exception.getMessage());
	}

	@Test
	@DisplayName("Verificando canditado a prefeito com função de vereador")
	void cenarioFuncaoErradaPrefeito() {

		Candidato candidatoPrefeito = new Candidato(1L, "Benício Viella", "", "48", 2, Candidato.Status.ATIVO, null);

		Candidato candidatoVereador = new Candidato(2L, "Giulia Santori", "", "48488", 2, Candidato.Status.ATIVO, null);

		Voto voto = new Voto(1, null, null, candidatoPrefeito, candidatoVereador);

		Exception exception = assertThrows(Exception.class, () -> {
			votoService.verificarCandidatos(voto);
		});

		assertEquals("O candidato escolhido para prefeito não é um candidato a prefeito. Refaça a requisição!",
				exception.getMessage());
	}

	@Test
	@DisplayName("Verificando canditado a vereador com função de prefeito")
	void cenarioFuncaoErradaVereador() {

		Candidato candidatoPrefeito = new Candidato(1L, "Benício Viella", "", "48", 1, Candidato.Status.ATIVO, null);

		Candidato candidatoVereador = new Candidato(2L, "Giulia Santori", "", "48488", 1, Candidato.Status.ATIVO, null);

		Voto voto = new Voto(1, null, null, candidatoPrefeito, candidatoVereador);

		Exception exception = assertThrows(Exception.class, () -> {
			votoService.verificarCandidatos(voto);
		});

		assertEquals("O candidato escolhido para vereador não é um candidato a vereador. Refaça a requisição!",
				exception.getMessage());
	}

	@Test
	@DisplayName("Verificando canditado a prefeito inativo")
	void cenarioPrefeitoInativo() {

		Candidato candidatoPrefeito = new Candidato(1L, "Benício Viella", "", "48", 1, Candidato.Status.INATIVO, null);

		Candidato candidatoVereador = new Candidato(2L, "Giulia Santori", "", "48488", 2, Candidato.Status.ATIVO, null);

		Voto voto = new Voto(1, null, null, candidatoPrefeito, candidatoVereador);

		Exception exception = assertThrows(Exception.class, () -> {
			votoService.verificarCandidatos(voto);
		});

		assertEquals("O candidato escolhido para prefeito está inativo. Refaça a requisição!", exception.getMessage());
	}

	@Test
	@DisplayName("Verificando canditado a vereador inativo")
	void cenarioVereadorInativo() {

		Candidato candidatoPrefeito = new Candidato(1L, "Benício Viella", "", "48", 1, Candidato.Status.ATIVO, null);

		Candidato candidatoVereador = new Candidato(2L, "Giulia Santori", "", "48488", 2, Candidato.Status.INATIVO,
				null);

		Voto voto = new Voto(1, null, null, candidatoPrefeito, candidatoVereador);

		Exception exception = assertThrows(Exception.class, () -> {
			votoService.verificarCandidatos(voto);
		});

		assertEquals("O candidato escolhido para vereador está inativo. Refaça a requisição!", exception.getMessage());
	}

}
