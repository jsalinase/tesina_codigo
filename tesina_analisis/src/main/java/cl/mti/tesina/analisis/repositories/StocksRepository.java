package cl.mti.tesina.analisis.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cl.mti.tesina.analisis.entities.Stocks;
import cl.mti.tesina.analisis.entities.StocksId;

@Repository
public interface StocksRepository extends JpaRepository<Stocks, StocksId>
{

}
