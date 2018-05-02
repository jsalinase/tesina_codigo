package cl.mti.tesina.pricing.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cl.mti.tesina.pricing.entities.Stocks;
import cl.mti.tesina.pricing.entities.StocksId;

@Repository
public interface StocksRepository extends JpaRepository<Stocks, StocksId>
{

}
