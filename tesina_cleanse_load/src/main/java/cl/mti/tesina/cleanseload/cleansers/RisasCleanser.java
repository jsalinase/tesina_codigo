/**
 * Elimina hashTags de las Noticias
 */

package cl.mti.tesina.cleanseload.cleansers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(value = Ordered.HIGHEST_PRECEDENCE)
public class RisasCleanser implements Cleanser
{
	@Override
	public String cleanse(String texto)
	{
		log.debug("### Limpiando Risas");
		try
		{
			Pattern risasTagPattern = Pattern.compile("\\b(a*ha+h[ha]*|o?l+o+l+[ol]*)\\b");
			Matcher m = risasTagPattern.matcher(texto);
			int i = 0;
			while (m.find())
			{
				texto = texto.replaceAll(m.group(i), "").trim();
				i++;
			}
			risasTagPattern = Pattern.compile("\\b(a*ja+j[ja]*)\\b");
			m = risasTagPattern.matcher(texto);
			i = 0;
			while (m.find())
			{
				texto = texto.replaceAll(m.group(i), "").trim();
				i++;
			}
		}
		catch (Exception ex)
		{
			log.error("Error procesando Risas", ex);
		}
		return texto;

	}

}
