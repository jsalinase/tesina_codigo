package cl.mti.tesina.analisis.dto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class Estadisticas
{
	private static final Logger log = LoggerFactory.getLogger(Estadisticas.class);

	private volatile int leidos;
	private volatile int grabados;
	private volatile int errores;

	public synchronized void incrementLeidos()
	{
		leidos++;
		if (leidos % 1000 == 0)
		{
			log.info(this + "");
		}
	}

	public synchronized void incrementErrores()
	{
		errores++;
	}

	public synchronized void incrementGrabados()
	{
		grabados++;
	}

	public int getLeidos()
	{
		return leidos;
	}

	public void setLeidos(int leidos)
	{
		this.leidos = leidos;
	}

	public int getGrabados()
	{
		return grabados;
	}

	public void setGrabados(int grabados)
	{
		this.grabados = grabados;
	}

	public int getErrores()
	{
		return errores;
	}

	public void setErrores(int errores)
	{
		this.errores = errores;
	}

	@Override
	public String toString()
	{
		return "Leidos=" + leidos + ", Grabados=" + grabados + ", Errores=" + errores;
	}

}
