package app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.entity.Candidato;
import app.repository.CandidatoRepository;

@Service
public class CandidatoService {

    @Autowired
    private CandidatoRepository candidatoRepository;

    // Cadastra um novo candidato com status ativo
    public Candidato cadastrarCandidato(Candidato candidato) {
        candidato.setStatus(Candidato.Status.ATIVO);
        return candidatoRepository.save(candidato);
    }

    // Lista apenas os candidatos ativos
    public List<Candidato> listarCandidatosAtivos() {
        return candidatoRepository.findByStatus(Candidato.Status.ATIVO);
    }

    public List<Candidato> getPrefeitosAtivos() {
        return candidatoRepository.findByFuncaoAndStatus(Candidato.Funcao.PREFEITO, Candidato.Status.ATIVO);
    }
 
    public List<Candidato> getVereadoresAtivos() {
        return candidatoRepository.findByFuncaoAndStatus(Candidato.Funcao.VEREADOR, Candidato.Status.ATIVO);
    }

    // Desativa candidato
    public void inativaCandidato(Long id) {
        Candidato candidato = candidatoRepository.findById(id).orElse(null);
        if (candidato == null) {
            throw new RuntimeException("Candidato não encontrado");
        }
        candidato.setStatus(Candidato.Status.INATIVO);
        candidatoRepository.save(candidato);
    }
}
