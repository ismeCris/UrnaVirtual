package app.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import app.entity.Candidato;

@Repository
public interface CandidatoRepository extends JpaRepository<Candidato, Long> {

	public List<Candidato> findByStatus(Candidato.Status status);

	public List<Candidato> findByFuncaoAndStatus(Integer funcao, Candidato.Status status);
	
	public Optional<Candidato> findByNumeroAndStatus(String numero, Candidato.Status status);
	
	public Optional<Candidato> findByNumero(String numero);
	
	Optional<Candidato> findByCpf(String cpf);
}
