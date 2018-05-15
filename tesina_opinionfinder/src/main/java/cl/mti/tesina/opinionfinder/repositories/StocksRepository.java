package cl.mti.tesina.opinionfinder.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cl.mti.tesina.opinionfinder.entities.Stocks;
import cl.mti.tesina.opinionfinder.entities.StocksId;

@Repository
public interface StocksRepository extends JpaRepository<Stocks, StocksId>
{

}
