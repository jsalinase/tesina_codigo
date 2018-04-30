package cl.mti.tesina.cleanseload.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
public class Noticias
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String titulo;
	@Column(columnDefinition = "LONGTEXT")
	private String texto;
	@Column(name="texto_procesado", columnDefinition = "LONGTEXT")
	private String textoProcesado;
	private Date fecha;
	private String fuente;
	private String recurso;
	private String autor;

	public String getAutor()
	{
		return autor;
	}

	public void setAutor(String autor)
	{
		this.autor = autor;
	}

	public String getRecurso()
	{
		return recurso;
	}

	public void setRecurso(String recurso)
	{
		this.recurso = recurso;
	}

	public Integer getId()
	{
		return id;
	}

	public void setId(Integer id)
	{
		this.id = id;
	}

	public String getTitulo()
	{
		return titulo;
	}

	public void setTitulo(String titulo)
	{
		this.titulo = titulo;
	}

	public String getTexto()
	{
		return texto;
	}

	public void setTexto(String texto)
	{
		this.texto = texto;
	}

	public Date getFecha()
	{
		return fecha;
	}

	public void setFecha(Date fecha)
	{
		this.fecha = fecha;
	}

	public String getFuente()
	{
		return fuente;
	}

	public void setFuente(String fuente)
	{
		this.fuente = fuente;
	}

	public String getTextoProcesado()
	{
		return textoProcesado;
	}

	public void setTextoProcesado(String textoProcesado)
	{
		this.textoProcesado = textoProcesado;
	}

	@Override
	public String toString()
	{
		return "Noticias [id=" + id + ", titulo=" + titulo + ", texto=" + texto + ", textoProcesado=" + textoProcesado
				+ ", fecha=" + fecha + ", fuente=" + fuente + ", recurso=" + recurso + ", autor=" + autor + "]";
	}

}
