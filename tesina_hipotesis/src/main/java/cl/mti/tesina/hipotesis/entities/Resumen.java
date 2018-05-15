package cl.mti.tesina.hipotesis.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

@Entity
@Data
public class Resumen
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String accion;
	private Integer idMotor;
	private boolean ok = false;
	private String sentimiento;
	private Float umbral;
	private Date fecha;

}
