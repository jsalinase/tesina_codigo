package cl.mti.tesina.hipotesis.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cl.mti.tesina.hipotesis.entities.Noticias;
import cl.mti.tesina.hipotesis.entities.Sentimientos;

@Repository
public interface SentimientosRepository extends JpaRepository<Sentimientos, Integer>
{
	List<Sentimientos> findByIdMotorAndNoticia(Integer idMotor, Noticias n);
}