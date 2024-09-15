package app.serviceTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import app.entity.Candidato.Status;
import app.entity.Eleitor;
import app.repository.EleitorRepository;
import app.service.EleitorService;

@SpringBootTest
public class EleitorServiceTest {

	@Autowired
	EleitorService eleitorService;

	@MockBean
	EleitorRepository eleitorRepository;

	@BeforeEach
	void setup() {
		Eleitor eleitor = new Eleitor();
		eleitor.setNomeCompleto("João da Silva");
		eleitor.setCpf("123.456.789-00");
		eleitor.setProfissao("Engenheiro");
		eleitor.setCelular("(11) 91234-5678");
		eleitor.setEmail("joao@teste.com");
		eleitor.setStatus(Eleitor.Status.APTO);
	}

	
	@Test
	void FindAll() {
		Eleitor eleitor1 = new Eleitor();
		eleitor1.setStatus(Eleitor.Status.APTO);

		Eleitor eleitor2 = new Eleitor();
		eleitor2.setStatus(Eleitor.Status.BLOQUEADO);

		Eleitor eleitor3 = new Eleitor();
		eleitor3.setStatus(Eleitor.Status.INATIVO);
		// Este deve ser ignorado

		when(eleitorRepository.findByStatusNot(Eleitor.Status.INATIVO)).thenReturn(List.of(eleitor1, eleitor2));

		List<Eleitor> eleitores = eleitorService.findAll();
		assertEquals(2, eleitores.size());
		assertTrue(eleitores.contains(eleitor1));
		assertTrue(eleitores.contains(eleitor2));
	}

	@Test
	void SaveStatusPendente() {

		Eleitor eleitor = new Eleitor();
		eleitor.setNomeCompleto("Ana Costa");
		eleitor.setCpf(null);
		eleitor.setEmail(null);

		when(eleitorRepository.save(any(Eleitor.class))).thenReturn(eleitor);

		String result = eleitorService.save(eleitor);
		assertEquals("Eleitor cadastrado", result);
		assertEquals(Eleitor.Status.PENDENTE, eleitor.getStatus());
	}

	@Test
	void UpdateEleitorNotFound() {
		Eleitor eleitor = new Eleitor();
		when(eleitorRepository.findById(1L)).thenReturn(Optional.empty());

		RuntimeException thrownException = assertThrows(RuntimeException.class, () -> {
			eleitorService.update(eleitor, 1L);
		});

		assertEquals("Eleitor não encontrado", thrownException.getMessage());
	}

	@Test
	void UpdateInativo() {

		Eleitor eleitorInDb = new Eleitor();
		eleitorInDb.setNomeCompleto("João da Silva");
		eleitorInDb.setCpf("123.456.789-00");
		eleitorInDb.setProfissao("Engenheiro");
		eleitorInDb.setCelular("(11) 91234-5678");
		eleitorInDb.setEmail("joao@teste.com");
		eleitorInDb.setStatus(Eleitor.Status.INATIVO);

		when(eleitorRepository.findById(1L)).thenReturn(Optional.of(eleitorInDb));

		Eleitor eleitorToUpdate = new Eleitor();
		eleitorToUpdate.setNomeCompleto("João da Silva");
		eleitorToUpdate.setCpf("123.456.789-00");
		eleitorToUpdate.setProfissao("Engenheiro");
		eleitorToUpdate.setCelular("(11) 91234-5678");
		eleitorToUpdate.setEmail("joao@teste.com");

		RuntimeException thrownException = assertThrows(RuntimeException.class, () -> {
			eleitorService.update(eleitorToUpdate, 1L);
		});

		assertEquals("O eleitor está inativo, portanto, não pode ser atualizado", thrownException.getMessage());
	}

	@Test
	void UpdateStatusBloqueado() {

		Eleitor eleitorInDb = new Eleitor();
		eleitorInDb.setNomeCompleto("Maria Oliveira");
		eleitorInDb.setCpf("987.654.321-00");
		eleitorInDb.setProfissao("Advogado");
		eleitorInDb.setCelular("(21) 98765-4321");
		eleitorInDb.setEmail("maria@teste.com");
		eleitorInDb.setStatus(Eleitor.Status.BLOQUEADO);

		when(eleitorRepository.findById(1L)).thenReturn(Optional.of(eleitorInDb));

		Eleitor eleitorToUpdate = new Eleitor();
		eleitorToUpdate.setNomeCompleto("Maria Oliveira");
		eleitorToUpdate.setCpf("987.654.321-00");
		eleitorToUpdate.setProfissao("Advogado");
		eleitorToUpdate.setCelular("(21) 98765-4321");
		eleitorToUpdate.setEmail("maria@teste.com");

		String result = eleitorService.update(eleitorToUpdate, 1L);
		assertEquals("Eleitor atualizado", result);
	}

	@Test
	void UpdateStatusApto() {

		Eleitor eleitorInDb = new Eleitor();
		eleitorInDb.setNomeCompleto("Carlos Santos");
		eleitorInDb.setCpf("111.222.333-44");
		eleitorInDb.setProfissao("Médico");
		eleitorInDb.setCelular("(31) 91234-5678");
		eleitorInDb.setEmail("carlos@teste.com");
		eleitorInDb.setStatus(Eleitor.Status.APTO);

		when(eleitorRepository.findById(1L)).thenReturn(Optional.of(eleitorInDb));

		Eleitor eleitorToUpdate = new Eleitor();
		eleitorToUpdate.setNomeCompleto("Carlos Santos");
		eleitorToUpdate.setCpf("111.222.333-44");
		eleitorToUpdate.setProfissao("Médico");
		eleitorToUpdate.setCelular("(31) 91234-5678");
		eleitorToUpdate.setEmail("carlos@teste.com");

		String result = eleitorService.update(eleitorToUpdate, 1L);
		assertEquals("Eleitor atualizado", result);
	}

	@Test
	void FindByIdNotFound() {

		when(eleitorRepository.findById(1L)).thenReturn(Optional.empty());

		RuntimeException thrownException = assertThrows(RuntimeException.class, () -> {
			eleitorService.findById(1L);
		});

		assertEquals("Eleitor não encontrado", thrownException.getMessage());
	}

	@Test
	void DeleteEleitorQueJaVotou() {
		Eleitor eleitorInDb = new Eleitor();
		eleitorInDb.setId(1L);
		eleitorInDb.setStatus(Eleitor.Status.VOTOU);

		when(eleitorRepository.findById(1L)).thenReturn(Optional.of(eleitorInDb));

		RuntimeException thrownException = assertThrows(RuntimeException.class, () -> {
			eleitorService.delete(1L);
		});

		assertEquals("O eleitor já votou, portanto, não pode ser inativado", thrownException.getMessage());
	}

	@Test
	void CpfOuEmailFaltando() {

		Eleitor eleitor = new Eleitor();
		eleitor.setCpf(null);
		eleitor.setEmail("email@teste.com");

		assertTrue(eleitorService.isPendente(eleitor));
	}

	@Test
	void CpfOuEmailVazio() {

		Eleitor eleitor = new Eleitor();
		eleitor.setCpf("");
		eleitor.setEmail("");

		assertTrue(eleitorService.isPendente(eleitor));
	}

	@Test
	void UpdateStatusVotou() {
		Eleitor eleitorInDb = new Eleitor();
		eleitorInDb.setId(1L);
		eleitorInDb.setStatus(Eleitor.Status.VOTOU);

		when(eleitorRepository.findById(1L)).thenReturn(Optional.of(eleitorInDb));

		Eleitor eleitorToUpdate = new Eleitor();
		eleitorToUpdate.setId(1L);
		eleitorToUpdate.setStatus(Eleitor.Status.APTO); // Tentativa de alteração de status

		Exception exception = assertThrows(RuntimeException.class, () -> {
			eleitorService.update(eleitorToUpdate, 1L);
		});

		assertEquals("O eleitor já votou, portanto, não pode ser atualizado", exception.getMessage());
	}

	@Test
	void DeleteEleitorNotFound() {
		when(eleitorRepository.findById(1L)).thenReturn(Optional.empty());

		RuntimeException thrownException = assertThrows(RuntimeException.class, () -> {
			eleitorService.delete(1L);
		});

		assertEquals("Eleitor não encontrado", thrownException.getMessage());
	}

	@Test
	void DeleteEleitorSucesso() {

		Eleitor eleitorInDb = new Eleitor();
		eleitorInDb.setId(2L);
		eleitorInDb.setStatus(Eleitor.Status.APTO);

		when(eleitorRepository.findById(2L)).thenReturn(Optional.of(eleitorInDb));

		String result = eleitorService.delete(2L);

		assertEquals("Eleitor desativado", result);
		assertEquals(Eleitor.Status.INATIVO, eleitorInDb.getStatus());
	}
	



}
