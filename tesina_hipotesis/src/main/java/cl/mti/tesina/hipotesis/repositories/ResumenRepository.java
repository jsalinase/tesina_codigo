package cl.mti.tesina.hipotesis.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cl.mti.tesina.hipotesis.entities.Resumen;

@Repository
public interface ResumenRepository extends JpaRepository<Resumen, Integer>
{
	
}
