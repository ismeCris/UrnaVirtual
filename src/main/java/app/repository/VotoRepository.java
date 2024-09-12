package app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import app.entity.Voto;

@Repository
public interface VotoRepository extends JpaRepository<Voto, Long> {

	@Query("SELECT COUNT(v) FROM Voto v WHERE v.prefeito.id = :candidatoId")
	int contaVotosPrefeito(@Param("candidatoId") Long candidatoId);

	@Query("SELECT COUNT(v) FROM Voto v WHERE v.vereador.id = :candidatoId")
	int contaVotosVereador(@Param("candidatoId") Long candidatoId);

}
