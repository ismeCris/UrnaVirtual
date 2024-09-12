package app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import app.entity.Voto;

@Repository
public interface VotoRepository extends JpaRepository<Voto, Long> {

	// Contar votos por candidato
	@Query("SELECT COUNT(v) FROM Voto v WHERE v.prefeito.id = :candidatoId OR v.vereador.id = :candidatoId")
	int countVotosByCandidato(@Param("candidatoId") Long candidatoId);

}
