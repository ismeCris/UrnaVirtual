package app.service;

import java.time.LocalDateTime;
import java.util.Comparator;
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

	    if (eleitor == null) {
	        throw new RuntimeException("Eleitor não encontrado");
	    }

	    if (eleitor.getStatus() != Eleitor.Status.APTO) {
	        throw new RuntimeException("Eleitor inapto para votação");
	    }

	    if (voto.getPrefeito() == null || voto.getVereador() == null) {
	        throw new RuntimeException("Os candidatos para prefeito e vereador devem ser selecionados");
	    }

	    Candidato prefeito = voto.getPrefeito();
	    Candidato vereador = voto.getVereador();

	    System.out.println("Função do prefeito: " + prefeito.getFuncao());
	    System.out.println("Função do vereador: " + vereador.getFuncao());

	    if (prefeito.getFuncao() == null) {
	        throw new RuntimeException("A função do candidato a prefeito é nula");
	    }

	    if (vereador.getFuncao() == null) {
	        throw new RuntimeException("A função do candidato a vereador é nula");
	    }

	    if (!prefeito.getFuncao().equals(1)) {
	        throw new RuntimeException("O candidato escolhido para prefeito não é um candidato a prefeito. Refaça a requisição!");
	    }

	    if (!vereador.getFuncao().equals(2)) {
	        throw new RuntimeException("O candidato escolhido para vereador não é um candidato a vereador. Refaça a requisição!");
	    }

	    if (!vereador.getFuncao().equals(2)) {
	        throw new RuntimeException("O candidato escolhido para vereador não é um candidato a vereador. Refaça a requisição!");
	    }
	    String hash = UUID.randomUUID().toString();
	    voto.setComprovante(hash);
	    voto.setDataHora(LocalDateTime.now());

	    votoRepository.save(voto);

	    eleitor.setStatus(Eleitor.Status.INATIVO);
	    eleitorService.definirStatus(eleitor);

	    return hash;
	}


	public Apuracao realizarAapuracao() {

		Apuracao apuracao = new Apuracao();
		List<Candidato> prefeotosAti = candidatoService.prefeitosAtivos();
		List<Candidato> vereadorAti = candidatoService.vereadoresAtivos();

		for (Candidato prefeito : prefeotosAti) {
			int totalVotos = votoRepository.contaVotosPrefeito(prefeito.getId());
			prefeito.setVotosApurados(totalVotos);
		}
		for (Candidato vereador : vereadorAti) {
			int totalVotos = votoRepository.contaVotosVereador(vereador.getId());
			vereador.setVotosApurados(totalVotos);
		}

		// Ordenar listas pelo total de votos
		Comparator<Candidato> porVotosApuradosDesc = Comparator.comparingInt(Candidato::getVotosApurados).reversed();

		prefeotosAti.sort(porVotosApuradosDesc);
		vereadorAti.sort(porVotosApuradosDesc);

		return apuracao;
	}

}
