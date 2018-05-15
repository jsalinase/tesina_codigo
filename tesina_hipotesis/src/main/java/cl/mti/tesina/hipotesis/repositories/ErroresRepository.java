package cl.mti.tesina.hipotesis.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cl.mti.tesina.hipotesis.entities.Errores;

@Repository
public interface ErroresRepository extends JpaRepository<Errores, Integer>
{

}
