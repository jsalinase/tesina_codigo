package cl.mti.tesina.hipotesis.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cl.mti.tesina.hipotesis.entities.Precios;

@Repository
public interface PreciosRepository extends JpaRepository<Precios, Integer>
{
	List<Precios> findByAccionAndFecha(String accion, Date fecha);
}
