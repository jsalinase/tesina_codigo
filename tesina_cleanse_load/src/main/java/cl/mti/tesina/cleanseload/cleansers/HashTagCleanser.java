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
public class HashTagCleanser implements Cleanser
{
	@Override
	public String cleanse(String texto)
	{
		log.debug("### Limpiando HashTags");
		try
		{
			Pattern hashTagPattern = Pattern.compile("#(\\w+)");
			Matcher m = hashTagPattern.matcher(texto);
			int i = 0;
			while (m.find())
			{
				texto = texto.replaceAll(m.group(i), "").trim();
				i++;
			}
		}
		catch (Exception ex)
		{
			
		}
		return texto;
	}

}
