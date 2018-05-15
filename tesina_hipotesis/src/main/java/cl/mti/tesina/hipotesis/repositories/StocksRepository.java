package cl.mti.tesina.hipotesis.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cl.mti.tesina.hipotesis.entities.Stocks;
import cl.mti.tesina.hipotesis.entities.StocksId;

@Repository
public interface StocksRepository extends JpaRepository<Stocks, StocksId>
{
	Page<Stocks> findByStocksIdFuente(String accion, Pageable request);
}
