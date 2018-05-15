/**
 * Elimina hashTags de las Noticias
 */

package cl.mti.tesina.cleanseload.cleansers;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(value = Ordered.HIGHEST_PRECEDENCE)
public class VocalesMultiplesCleanser implements Cleanser
{
	@Override
	public String cleanse(String texto)
	{
		log.debug("### Limpiando Vocales Multiples");
		try
		{
			if (texto != null)
			{
				texto = texto.replaceAll("(.)\\1{3,}", "$1");
			}
		}
		catch (Exception ex)
		{
			log.error("Error procesando Vocales", ex);
		}
		return texto;
	}
}
