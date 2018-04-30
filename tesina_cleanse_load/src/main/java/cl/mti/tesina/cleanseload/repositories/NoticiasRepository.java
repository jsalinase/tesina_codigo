package cl.mti.tesina.cleanseload.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cl.mti.tesina.cleanseload.entities.Noticias;

@Repository
public interface NoticiasRepository extends JpaRepository<Noticias, Integer> {
	}
