package app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import app.entity.Candidato;

@Repository
public interface CandidatoRepository extends JpaRepository<Candidato, Long> {

	List<Candidato> findByStatus(Candidato.Status status);

	List<Candidato> findByFuncaoAndStatus(Candidato.Funcao funcao, Candidato.Status status);
}
