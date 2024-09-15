package app.controllerTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import app.controller.VotoController;
import app.entity.Apuracao;
import app.entity.Candidato;
import app.entity.Eleitor;
import app.entity.Voto;
import app.repository.VotoRepository;
import app.service.CandidatoService;
import app.service.EleitorService;

@SpringBootTest
public class VotoControllerTest {
	
	@Autowired
	VotoController votoController;
	
	@MockBean
	VotoRepository votoRepository;
	
	@MockBean
	EleitorService eleitorService;
	
	@MockBean 
	CandidatoService candidatoService;
	
	@BeforeEach
	void setUp() {
		
		Eleitor eleitor = new Eleitor(1, "Mikael Boreski", "198.927.837-28", "Biólogo", "(11) 93784- 7834", null,
				"mikaelbiologo@gmail.com", Eleitor.Status.APTO);
		
		Eleitor eleitorInapto = new Eleitor(2, "Paola Álvares", "845.383.593-49", "Chef de cozinha", "(41) 98723- 6372", null,
				"alvspaola@hotmail.com", Eleitor.Status.BLOQUEADO);
		
		Candidato candidatoPrefeito = new Candidato(1L, "Benício Viella", "", "48", 1, Candidato.Status.ATIVO, null);

		Candidato candidatoVereador = new Candidato(2L, "Giulia Santori", "", "48488", 2, Candidato.Status.ATIVO, null);
		
		List<Candidato> prefeitosAtivos = new ArrayList<>();
		prefeitosAtivos.add(candidatoPrefeito);
		
		List<Candidato> vereadoresAtivos = new ArrayList<>();
		vereadoresAtivos.add(candidatoVereador);

		Voto voto = new Voto(1, null, null, candidatoPrefeito, candidatoVereador);
		
		Mockito.when(votoRepository.save(any())).thenReturn(voto);
		
		Mockito.when(eleitorService.findById(0)).thenReturn(null);
		
		Mockito.when(eleitorService.findById(1)).thenReturn(eleitor);
		
		Mockito.when(eleitorService.findById(2)).thenReturn(eleitorInapto);
		
		Mockito.when(eleitorService.update(Mockito.any(), Mockito.anyLong())).thenReturn("Eleitor atualizado");
		
		Mockito.when(candidatoService.prefeitosAtivos()).thenReturn(prefeitosAtivos);
		
		Mockito.when(candidatoService.vereadoresAtivos()).thenReturn(vereadoresAtivos);
		
		Mockito.when(votoRepository.contaVotosPrefeito(1L)).thenReturn(1);
		
		Mockito.when(votoRepository.contaVotosPrefeito(2L)).thenReturn(1);
		
	}
	
	@Test
	@DisplayName("Integração - Salvando um voto")
	void cenarioVoto() {
		
		Candidato candidatoPrefeito = new Candidato(1L, "Benício Viella", "", "48", 1, Candidato.Status.ATIVO, null);

		Candidato candidatoVereador = new Candidato(2L, "Giulia Santori", "", "48488", 2, Candidato.Status.ATIVO, null);

		Voto voto = new Voto(1, null, null, candidatoPrefeito, candidatoVereador);
		
		ResponseEntity<String> retorno = this.votoController.votar(voto, 1L);
		
		assertEquals(HttpStatus.CREATED, retorno.getStatusCode());
	}
	
	@Test
	@DisplayName("Integração - Bad Request eleitor não encontrado")
	void cenarioVotoEleitorNotFound() {
		
		Candidato candidatoPrefeito = new Candidato(1L, "Benício Viella", "", "48", 1, Candidato.Status.ATIVO, null);

		Candidato candidatoVereador = new Candidato(2L, "Giulia Santori", "", "48488", 2, Candidato.Status.ATIVO, null);

		Voto voto = new Voto(1, null, null, candidatoPrefeito, candidatoVereador);
		
		ResponseEntity<String> retorno = this.votoController.votar(voto, 0L);
		String message = retorno.getBody();
		
		assertEquals(HttpStatus.BAD_REQUEST, retorno.getStatusCode());
		assertEquals("Eleitor não encontrado", message);
	}
	
	
	@Test
	@DisplayName("Integração - Bad Request eleitor inapto")
	void cenarioVotoEleitorInapto() {
		
		Candidato candidatoPrefeito = new Candidato(1L, "Benício Viella", "", "48", 1, Candidato.Status.ATIVO, null);
		
		Candidato candidatoVereador = new Candidato(2L, "Giulia Santori", "", "48488", 2, Candidato.Status.ATIVO, null);
		
		Voto voto = new Voto(1, null, null, candidatoPrefeito, candidatoVereador);
		
		ResponseEntity<String> retorno = this.votoController.votar(voto, 2L);
		String message = retorno.getBody();
		
		assertEquals(HttpStatus.BAD_REQUEST, retorno.getStatusCode());
		assertEquals("Eleitor inapto para votação", message);
	}
	
	@Test
	@DisplayName("Integração - Realizar Apuração dos votos")
	void cenarioRealizarApuracao() {
		
		ResponseEntity<Apuracao> retorno = this.votoController.realizarApuracao();
		Apuracao apuracao = retorno.getBody();
		
		assertEquals(1, apuracao.getTotalVotos());
		
	}
}
