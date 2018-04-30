package cl.mti.tesina.cleanseload.cleansers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cl.mti.tesina.cleanseload.workers.CleanseLoadNoticias;

public interface Cleanser
{
	static final Logger log = LoggerFactory.getLogger(CleanseLoadNoticias.class);

	String cleanse(String texto);
}
