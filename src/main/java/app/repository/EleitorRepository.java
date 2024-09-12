package app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import app.entity.Eleitor;
import app.entity.Eleitor.Status;

public interface EleitorRepository extends JpaRepository<Eleitor, Long>{

	public List<Eleitor> findByStatusNot(Status status);
}
