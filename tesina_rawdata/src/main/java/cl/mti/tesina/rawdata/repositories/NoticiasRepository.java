package cl.mti.tesina.rawdata.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cl.mti.tesina.rawdata.entities.Noticias;

@Repository
public interface NoticiasRepository extends JpaRepository<Noticias, Integer> {

}
