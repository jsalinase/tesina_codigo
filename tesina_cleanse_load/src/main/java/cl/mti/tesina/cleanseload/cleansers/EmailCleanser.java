/**
 * Elimina Url de las Noticias
 */

package cl.mti.tesina.cleanseload.cleansers;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(value = Ordered.HIGHEST_PRECEDENCE)
public class EmailCleanser implements Cleanser
{
	private String EMAIL_PATTERN = "([^.@\\s]+)(\\.[^.@\\s]+)*@([^.@\\s]+\\.)+([^.@\\s]+)";

	@Override
	public String cleanse(String texto)
	{
		log.debug("### Limpiando Emails");

		try
		{
			texto = texto.replaceAll(EMAIL_PATTERN, "");
		}
		catch (Exception ex)
		{
			log.error("Error procesando URLs: " + texto, ex);
		}
		return texto;
	}
}
