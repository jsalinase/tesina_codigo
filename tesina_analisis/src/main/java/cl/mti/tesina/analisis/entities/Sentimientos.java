package cl.mti.tesina.analisis.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Sentimientos
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@ManyToOne
	private Noticias noticia;

	private int idMotor;
	private String sentimiento;
	private Float puntajePositivo;
	private Float puntajeNegativo;
	private Float puntajeNeutral;
	private Float puntajeMixto;

	public Float getPuntajePositivo()
	{
		return puntajePositivo;
	}

	public void setPuntajePositivo(Float puntajePositivo)
	{
		this.puntajePositivo = puntajePositivo;
	}

	public Float getPuntajeNegativo()
	{
		return puntajeNegativo;
	}

	public void setPuntajeNegativo(Float puntajeNegativo)
	{
		this.puntajeNegativo = puntajeNegativo;
	}

	public Float getPuntajeNeutral()
	{
		return puntajeNeutral;
	}

	public void setPuntajeNeutral(Float puntajeNeutral)
	{
		this.puntajeNeutral = puntajeNeutral;
	}

	public Float getPuntajeMixto()
	{
		return puntajeMixto;
	}

	public void setPuntajeMixto(Float puntajeMixto)
	{
		this.puntajeMixto = puntajeMixto;
	}

	public Integer getId()
	{
		return id;
	}

	public void setId(Integer id)
	{
		this.id = id;
	}

	public Noticias getNoticia()
	{
		return noticia;
	}

	public void setNoticia(Noticias noticia)
	{
		this.noticia = noticia;
	}

	public int getIdMotor()
	{
		return idMotor;
	}

	public void setIdMotor(int idMotor)
	{
		this.idMotor = idMotor;
	}

	public String getSentimiento()
	{
		return sentimiento;
	}

	public void setSentimiento(String sentimiento)
	{
		this.sentimiento = sentimiento;
	}

	@Override
	public String toString()
	{
		return "Sentimientos [id=" + id + ", noticia=" + noticia + ", idMotor=" + idMotor + ", sentimiento="
				+ sentimiento + ", puntajePositivo=" + puntajePositivo + ", puntajeNegativo=" + puntajeNegativo
				+ ", puntajeNeutral=" + puntajeNeutral + ", puntajeMixto=" + puntajeMixto + "]";
	}

}
