/**
 * Elimina Emoticons de las Noticias
 */

package cl.mti.tesina.cleanseload.cleansers;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import javax.annotation.PostConstruct;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(value = Ordered.HIGHEST_PRECEDENCE)
public class EmoticonCleanse implements Cleanser
{

	private Map<String, String> mapaDiccionario = new HashMap<String, String>();

	@PostConstruct
	void init()
	{
		mapaDiccionario.put(":‑)", "i like this");
		mapaDiccionario.put(":)", "i like this");
		mapaDiccionario.put(":-]", "i like this");
		mapaDiccionario.put(":]", "i like this");
		mapaDiccionario.put(":-3", "i like this");
		mapaDiccionario.put(":3", "i like this");
		mapaDiccionario.put(":->", "i like this");
		mapaDiccionario.put(":>", "i like this");
		mapaDiccionario.put("8-)", "i like this");
		mapaDiccionario.put("8)", "i like this");
		mapaDiccionario.put(":-}", "i like this");
		mapaDiccionario.put(":}", "i like this");
		mapaDiccionario.put(":o)", "i like this");
		mapaDiccionario.put(":c)", "i like this");
		mapaDiccionario.put(":^)", "i like this");
		mapaDiccionario.put("=]", "i like this");
		mapaDiccionario.put("=)", "i like this");

		mapaDiccionario.put(":‑(", "i don't like this");
		mapaDiccionario.put(":(", "i don't like this");
		mapaDiccionario.put(":‑c", "i don't like this");
		mapaDiccionario.put(":c", "i don't like this");
		mapaDiccionario.put(":‑<", "i don't like this");
		mapaDiccionario.put(":<", "i don't like this");
		mapaDiccionario.put(":‑[", "i don't like this");
		mapaDiccionario.put(":[", "i don't like this");
		mapaDiccionario.put(":-||", "i don't like this");
		mapaDiccionario.put(">:[", "i don't like this");
		mapaDiccionario.put(":{", "i don't like this");
		mapaDiccionario.put(":@", "i don't like this");
		mapaDiccionario.put(">:(", "i don't like this");
		mapaDiccionario.put(":'‑(", "i don't like this");
		mapaDiccionario.put(":'(", "i don't like this");
	}

	@Override
	public String cleanse(String texto)
	{
		log.debug("### Limpiando Emoticones");
		StringBuilder sb = new StringBuilder();

		try
		{
			StringTokenizer t = new StringTokenizer(texto, " ");

			while (t.hasMoreTokens())
			{
				String tt = t.nextToken();

				String o = mapaDiccionario.get(tt);
				if (o != null)
				{
					sb.append(o + " ");
				}
				else
				{
					sb.append(tt + " ");
				}
			}
		}
		catch (Exception ex)
		{
			log.error("Error procesando Emoticones", ex);
			sb = new StringBuilder();
			sb.append(texto);
		}
		return sb.toString();

	}

}
