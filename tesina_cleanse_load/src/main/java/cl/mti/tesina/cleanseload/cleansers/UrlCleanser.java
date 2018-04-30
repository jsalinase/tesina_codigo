/**
 * Elimina Url de las Noticias
 */

package cl.mti.tesina.cleanseload.cleansers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(value = Ordered.HIGHEST_PRECEDENCE)
public class UrlCleanser implements Cleanser
{
	@Override
	public String cleanse(String texto)
	{
		log.debug("### Limpiando URL");

		try
		{
			int i = 0;
			while (true)
			{
				String urlPattern = "\\b(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
				Pattern p = Pattern.compile(urlPattern, Pattern.CASE_INSENSITIVE);
				Matcher m = p.matcher(texto);
				if (m.find())
				{
					texto = texto.replaceFirst(m.group(0), "").trim();
				}
				m = p.matcher(texto);
				if (!m.find())
				{
					break;
				}
				i++;
				if (i > 100000)
				{
					break;
				}
			}
		}
		catch (Exception ex)
		{
			log.error("Error procesando URLs: " + texto, ex);
		}
		return texto;
	}

}
