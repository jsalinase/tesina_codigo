package cl.mti.tesina.hipotesis.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;

@Data
@Entity
public class Precios
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String accion;
	@Temporal(TemporalType.DATE)
	private Date fecha;
	private Float close;
	private Float volume;
	private Float open;
	private Float high;
	private Float low;
	
	@Override
	public String toString()
	{
		return "Precios [id=" + id + ", accion=" + accion + ", fecha=" + fecha + ", close=" + close + ", volume="
				+ volume + ", open=" + open + ", high=" + high + ", low=" + low + "]";
	}
}
