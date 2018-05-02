package cl.mti.tesina.analisis.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cl.mti.tesina.analisis.entities.Errores;

@Repository
public interface ErroresRepository extends JpaRepository<Errores, Integer>
{

}
