package app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import app.entity.Voto;

public interface VotoRepository extends JpaRepository<Voto, Long>{

}
