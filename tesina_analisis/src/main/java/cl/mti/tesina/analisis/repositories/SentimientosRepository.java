package cl.mti.tesina.analisis.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cl.mti.tesina.analisis.entities.Sentimientos;

@Repository
public interface SentimientosRepository extends JpaRepository<Sentimientos, Integer>
{

}
