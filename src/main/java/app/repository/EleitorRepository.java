package app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import app.entity.Eleitor;

public interface EleitorRepository extends JpaRepository<Eleitor, Long>{

}
