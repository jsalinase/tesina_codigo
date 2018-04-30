/**
 * Elimina Dobles espacios y hace trim de las Noticias
 */

package cl.mti.tesina.cleanseload.cleansers;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(value = Ordered.LOWEST_PRECEDENCE)
public class GenericCleanser implements Cleanser
{
	@Override
	public String cleanse(String n)
	{
		log.debug("### Limpiando Generico");
		try
		{
			if (n != null)
			{
				n = n.trim().replaceAll("\\s+", " ");
			}
		}
		catch (Exception ex)
		{
			log.error("Error procesando Generico", ex);
		}
		return n;
	}

}
