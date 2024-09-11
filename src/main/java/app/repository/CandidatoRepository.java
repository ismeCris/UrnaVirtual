package app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import app.entity.Candidato;

public interface CandidatoRepository extends JpaRepository<Candidato, Long> {

}
