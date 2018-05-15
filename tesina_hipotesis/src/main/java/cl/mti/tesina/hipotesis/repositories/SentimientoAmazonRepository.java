package cl.mti.tesina.hipotesis.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cl.mti.tesina.hipotesis.entities.SentimientoAmazon;

@Repository
public interface SentimientoAmazonRepository extends JpaRepository<SentimientoAmazon, Integer>
{

}
