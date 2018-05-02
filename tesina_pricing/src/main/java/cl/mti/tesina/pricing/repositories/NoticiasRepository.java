package cl.mti.tesina.pricing.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cl.mti.tesina.pricing.entities.Noticias;

@Repository
public interface NoticiasRepository extends JpaRepository<Noticias, Integer>
{

}
