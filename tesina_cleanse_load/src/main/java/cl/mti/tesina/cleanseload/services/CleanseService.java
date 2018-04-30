/**
 * Servicio que Limpia las Noticias con todos los cleanser disponibles en el contexto
 */
package cl.mti.tesina.cleanseload.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cl.mti.tesina.cleanseload.cleansers.Cleanser;

@Component
public class CleanseService
{
	@Autowired
	private List<Cleanser> listadoCleanser;

	/**
	 * Limpia las Noticias con todos los cleansers disponibles
	 * 
	 * @param n
	 * @return
	 */
	public String cleanse(String n)
	{
		if (listadoCleanser != null)
		{
			for (Cleanser c : listadoCleanser)
			{
				n = c.cleanse(n);
			}
		}

		return n;
	}
}
