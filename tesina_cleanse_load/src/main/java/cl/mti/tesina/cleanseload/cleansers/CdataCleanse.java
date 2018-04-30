package cl.mti.tesina.cleanseload.cleansers;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(value = Ordered.LOWEST_PRECEDENCE)
public class CdataCleanse implements Cleanser
{

	@Override
	public String cleanse(String texto)
	{
		try
		{
			if (texto.contains("//<![CDATA["))
			{
				int indice = texto.indexOf("//<![CDATA[");
				texto = texto.substring(0, indice - 1);
			}
		}
		catch (Exception ex)
		{
			log.error("Error con la CDATA", ex);
		}
		return texto;
	}

}
