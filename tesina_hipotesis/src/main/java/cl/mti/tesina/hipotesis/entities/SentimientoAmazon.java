package cl.mti.tesina.hipotesis.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;

import cl.mti.tesina.hipotesis.dto.ResultadoSentimiento;

@Entity
public class SentimientoAmazon extends ResultadoSentimiento
{
	@Id
	protected int id;

	protected String sentimiento;
	protected Float positivo;
	protected Float negativo;
	protected Float neutral;
	protected Float mixto;
	protected Date fechaHora;
	protected int idNoticia;

	public int getIdNoticia()
	{
		return idNoticia;
	}

	public void setIdNoticia(int idNoticia)
	{
		this.idNoticia = idNoticia;
	}

	public Date getFechaHora()
	{
		return fechaHora;
	}

	public void setFechaHora(Date fechaHora)
	{
		this.fechaHora = fechaHora;
	}

	public String getSentimiento()
	{
		return sentimiento;
	}

	public void setSentimiento(String sentimiento)
	{
		this.sentimiento = sentimiento;
	}

	public Float getPositivo()
	{
		return positivo;
	}

	public void setPositivo(Float positivo)
	{
		this.positivo = positivo;
	}

	public Float getNegativo()
	{
		return negativo;
	}

	public void setNegativo(Float negativo)
	{
		this.negativo = negativo;
	}

	public Float getNeutral()
	{
		return neutral;
	}

	@Override
	public String toString()
	{
		return "SentimientoAmazon [id=" + id + ", sentimiento=" + sentimiento + ", positivo=" + positivo + ", negativo="
				+ negativo + ", neutral=" + neutral + ", mixto=" + mixto + "]";
	}

	public void setNeutral(Float neutral)
	{
		this.neutral = neutral;
	}

	public Float getMixto()
	{
		return mixto;
	}

	public void setMixto(Float mixto)
	{
		this.mixto = mixto;
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

}
