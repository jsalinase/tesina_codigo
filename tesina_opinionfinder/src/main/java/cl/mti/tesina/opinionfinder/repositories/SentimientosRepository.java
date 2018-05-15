package cl.mti.tesina.opinionfinder.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cl.mti.tesina.opinionfinder.entities.Noticias;
import cl.mti.tesina.opinionfinder.entities.Sentimientos;

@Repository
public interface SentimientosRepository extends JpaRepository<Sentimientos, Integer>
{
	List<Sentimientos> findByIdMotorAndNoticia(Integer idMotor, Noticias n);
}
