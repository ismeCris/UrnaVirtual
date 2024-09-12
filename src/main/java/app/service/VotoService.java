package app.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.entity.Apuracao;
import app.entity.Candidato;
import app.entity.Eleitor;
import app.entity.Voto;
import app.repository.VotoRepository;

@Service
public class VotoService {
	@Autowired
	VotoRepository votoRepository;

	@Autowired
	private EleitorService eleitorService;

	@Autowired
	private CandidatoService candidatoService;

	public String votar(Voto voto, Long eleitorId) {
		Eleitor eleitor = eleitorService.findById(eleitorId);

		// Verifique se o candidato a prefeito é válido
		if (!voto.getPrefeito().getFuncao().equals(Candidato.Funcao.PREFEITO)) {
			throw new RuntimeException(
					"O candidato escolhido para prefeito é um candidato a vereador. Refaça a requisição!");
		}

		// Verifique se o candidato a vereador é válido
		if (!voto.getVereador().getFuncao().equals(Candidato.Funcao.VEREADOR)) {
			throw new RuntimeException(
					"O candidato escolhido para vereador é um candidato a prefeito. Refaça a requisição!");
		}
		  // Gera o hash para o comprovante de votação
	    String hash = UUID.randomUUID().toString();
	    voto.setComprovante(hash);
	    
	    voto.setDataHora(LocalDateTime.now());
	    
	    votoRepository.save(voto);
	    
	    // Atualiza o status do eleitor para inativo
	    eleitor.setStatus(Eleitor.Status.INATIVO);
	    eleitorService.definirStatus(eleitor);
	    return hash; 
	    
	    }

}
