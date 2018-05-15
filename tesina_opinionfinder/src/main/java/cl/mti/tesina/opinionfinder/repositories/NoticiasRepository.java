package cl.mti.tesina.opinionfinder.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cl.mti.tesina.opinionfinder.entities.Noticias;

@Repository
public interface NoticiasRepository extends JpaRepository<Noticias, Integer> {

}
