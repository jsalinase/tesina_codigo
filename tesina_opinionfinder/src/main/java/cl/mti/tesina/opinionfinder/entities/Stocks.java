package cl.mti.tesina.opinionfinder.entities;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "stocks")
public class Stocks
{
	@EmbeddedId
	private StocksId stocksId;

	public StocksId getStocksId()
	{
		return stocksId;
	}

	public void setStocksId(StocksId stocksId)
	{
		this.stocksId = stocksId;
	}
}
