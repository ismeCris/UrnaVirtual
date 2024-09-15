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
		
		eleitor.setStatus(this.definirStatus(eleitor));

		this.eleitorRepository.save(eleitor);

		return "Eleitor cadastrado";
	}

	public String update(Eleitor eleitor, long id) {

		eleitor.setId(id);
		
		Eleitor eleitorInDb = this.findById(id);
		eleitor.setStatus(eleitorInDb.getStatus());
		eleitor.setStatus(this.definirStatus(eleitor));
		
		if(eleitor.getStatus().equals(Status.INATIVO)) {
			throw new RuntimeException("O eleitor está inativo, portanto, não pode ser atualizado");
		}

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
		
		if(eleitor.getStatus().equals(Status.VOTOU)) {
			throw new RuntimeException("O eleitor já votou, portanto, não pode ser inativado");
		}
		
		eleitor.setStatus(Status.INATIVO);

		this.update(eleitor, id);

		return "Eleitor desativado";
	}

	public Status definirStatus (Eleitor eleitor) {
		
		Status status;
		
		if(eleitor.getStatus() == null) {
			eleitor.setStatus(Status.PENDENTE);
		}
		
		if(eleitor.getStatus().equals(Status.INATIVO)) {
			status = Status.INATIVO;
		}else if (eleitor.getStatus().equals(Status.BLOQUEADO)) {
			status = Status.BLOQUEADO;
		} if(isPendente(eleitor)){
			 status = Status.PENDENTE;
		}else if (eleitor.getStatus().equals(Status.VOTOU)) {
			status = Status.VOTOU;
		}else {
			status = Status.APTO;
		}
		
		return status;
		
	}
	
	public boolean isPendente(Eleitor eleitor) {
		
		if(eleitor.getCpf() == null || eleitor.getEmail() == null) {
			return true;
		}
		
		if(eleitor.getCpf().isEmpty() || eleitor.getEmail().isEmpty()) {
			return true;
		}else {
			return false;
		}
		
	}
}
