package app.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.entity.Eleitor;
import app.entity.Eleitor.Status;
import app.repository.EleitorRepository;

@Service
public class EleitorService {

	@Autowired
	EleitorRepository eleitorRepository;

	public String save(Eleitor eleitor) {

		if (isPendente(eleitor)) {
			eleitor.setStatus(Status.PENDENTE);
		} else {
			eleitor.setStatus(Status.APTO);
		}

		this.eleitorRepository.save(eleitor);

		return "Eleitor cadastrado";
	}

	public String update(Eleitor eleitor, long id) {
		// Mantém o ID do eleitor e define outros atributos conforme necessário
		eleitor.setId(id);

		Status novoStatus = this.definirStatus(eleitor);
		eleitor.setStatus(novoStatus);

		this.eleitorRepository.save(eleitor);

		return "Eleitor atualizado";
	}

	public Eleitor findById(long id) {

		Optional<Eleitor> optional = this.eleitorRepository.findById(id);

		if (optional.isPresent()) {
			return optional.get();
		} else {
			throw new RuntimeException("Eleitor não encontrado");
		}
	}

	public List<Eleitor> findAll() {
		return this.eleitorRepository.findByStatusNot(Status.INATIVO);
	}

	public String delete(long id) {

		Eleitor eleitor = this.findById(id);

		if (eleitor.getStatus().equals(Status.VOTOU)) {
			throw new RuntimeException("O eleitor já votou, portanto, não pode ser inativado");
		}

		eleitor.setStatus(Status.INATIVO);

		// Atualiza diretamente no repositório sem chamar update(), pois o eleitor está
		// sendo inativado
		this.eleitorRepository.save(eleitor);

		return "Eleitor desativado";
	}

	public Status definirStatus(Eleitor eleitor) {

		// Busca o eleitor atual no banco de dados
		Eleitor eleitorInDb = this.findById(eleitor.getId());

		// Verifica se o eleitor está inativo
		if (eleitorInDb.getStatus().equals(Status.INATIVO)) {
			throw new RuntimeException("O eleitor está inativo, portanto, não pode ser atualizado");
		}

		Status status;

		if (eleitor.getStatus() == null) {
			eleitor.setStatus(Status.PENDENTE);
		}

		if (eleitorInDb.getStatus().equals(Status.BLOQUEADO)) {
			status = Status.BLOQUEADO;
		} else if (eleitor.getStatus().equals(Status.VOTOU)) {
			status = Status.VOTOU;
		} else if (isPendente(eleitor)) {
			status = Status.PENDENTE;
		} else {
			status = Status.APTO;
		}
		return status;

	}

	public boolean isPendente(Eleitor eleitor) {

		if (eleitor.getCpf() == null || eleitor.getEmail() == null) {
			return true;
		}

		if (eleitor.getCpf().isEmpty() || eleitor.getEmail().isEmpty()) {
			return true;
		} else {
			return false;
		}

	}
}
