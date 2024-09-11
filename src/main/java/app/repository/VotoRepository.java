package app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import app.entity.Voto;

@Repository
public interface VotoRepository extends JpaRepository<Voto, Long> {

}
