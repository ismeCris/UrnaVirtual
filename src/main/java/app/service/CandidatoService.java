package app.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.entity.Candidato;
import app.repository.CandidatoRepository;

@Service
public class CandidatoService {

	@Autowired
	private CandidatoRepository candidatoRepository;

	// Cadastra um novo candidato com status ativo
			public String cadastrarCandidato(Candidato candidato) {
				
				if(candidato.getFuncao() != 1 && candidato.getFuncao() != 2) {
					throw new RuntimeException("A função deve ser 1 para prefeito ou 2 para vereador");
				}
				confereDados(candidato);
			    candidato.setStatus(Candidato.Status.ATIVO);
			    candidatoRepository.save(candidato);
			    return "Candidato salvo com sucesso";
			}
			
		
		
	public String atualizar(Candidato candidato, Long id) {
		Optional<Candidato> candidatoOptional = candidatoRepository.findById(id);
		if (candidatoOptional.isEmpty()) {
			throw new RuntimeException("Candidato não encontrado com o ID: " + id);
		}
		Candidato candidatoExistente = candidatoOptional.get();
		confereDados(candidato);
		// Verificar o status do candidato existente
		if (candidatoExistente.getStatus().equals(Candidato.Status.INATIVO)) {
			throw new RuntimeException("O candidato está inativo, portanto, não pode ser atualizado");
		}

		candidatoExistente.setNome(candidato.getNome());
		candidatoExistente.setCpf(candidato.getCpf());
		candidatoExistente.setNumero(candidato.getNumero());
		candidatoExistente.setFuncao(candidato.getFuncao());

		candidatoRepository.save(candidatoExistente);

		return "Candidato atualizado com sucesso";
	}

	public Candidato findById(long id) {

		Optional<Candidato> optional = this.candidatoRepository.findById(id);

		if (optional.isPresent()) {
			return optional.get();
		} else {
			throw new RuntimeException("Candidato não encontrado");
		}
	}

	public Candidato findByNumero(String numero) {
		
		Optional<Candidato> optional = this.candidatoRepository.findByNumero(numero);
		
		if (optional.isPresent()) {
			return optional.get();
		} else {
			throw new RuntimeException("Candidato não encontrado");
		}
	}
	
	public List<Candidato> listarCandidatosAtivos() {
		return candidatoRepository.findByStatus(Candidato.Status.ATIVO);
	}

	public List<Candidato> prefeitosAtivos() {
	    return candidatoRepository.findByFuncaoAndStatus(1, Candidato.Status.ATIVO);  // 1 para Prefeito
	}

	public List<Candidato> vereadoresAtivos() {
	    return candidatoRepository.findByFuncaoAndStatus(2, Candidato.Status.ATIVO);  // 2 para Vereador
	}


	// Desativa candidato
	public String inativaCandidato(Long id) {
		Candidato candidato = candidatoRepository.findById(id).orElse(null);
		if (candidato == null) {
			throw new RuntimeException("Candidato não encontrado");
		}
		candidato.setStatus(Candidato.Status.INATIVO);
		candidatoRepository.save(candidato);
		return "Candidato desativado com sucesso";
	}
	
	public void confereDados(Candidato candidato) {
	    if (candidato.getFuncao() != 1 && candidato.getFuncao() != 2) {
	        throw new RuntimeException("A função deve ser 1 para prefeito ou 2 para vereador");
	    }

	    // Verifica o cpf e garante que o mesmo cpf seja usado se o candidato for o mesmo
	    Optional<Candidato> candidatoExistente = candidatoRepository.findByCpf(candidato.getCpf());
	    if (candidatoExistente.isPresent() && !candidatoExistente.get().getId().equals(candidato.getId())) {
	        throw new RuntimeException("O CPF " + candidato.getCpf() + " já está cadastrado para outro candidato.");
	    }

	    // Verifica se o número já está cadastrado para outro candidato
	    Optional<Candidato> candidatoExistentePorNumero = candidatoRepository.findByNumero(candidato.getNumero());
	    if (candidatoExistentePorNumero.isPresent() && 
	        (candidato.getId() == null || !candidatoExistentePorNumero.get().getId().equals(candidato.getId()))) {
	        throw new RuntimeException("O número " + candidato.getNumero() + " já está cadastrado para outro candidato.");
	    }

	    // Validação do número
	    if (candidato.getFuncao() == 1 && candidato.getNumero().length() != 2) {
	        throw new RuntimeException("O número do candidato a prefeito deve ter exatamente 2 caracteres.");
	    }
	    if (candidato.getFuncao() == 2 && candidato.getNumero().length() != 5) {
	        throw new RuntimeException("O número do candidato a vereador deve ter exatamente 5 caracteres.");
	    }
	}



}
