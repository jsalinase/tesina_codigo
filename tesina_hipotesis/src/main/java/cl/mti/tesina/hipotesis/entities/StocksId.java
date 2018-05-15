package cl.mti.tesina.hipotesis.entities;

import java.io.Serializable;

import javax.persistence.Embeddable;

@Embeddable
public class StocksId implements Serializable
{
	private int id;
	private String fuente;

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public String getFuente()
	{
		return fuente;
	}

	public void setFuente(String fuente)
	{
		this.fuente = fuente;
	}
}
