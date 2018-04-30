package cl.mti.tesina.analisis.workers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import cl.mti.tesina.analisis.dto.CodigosResultados;
import cl.mti.tesina.analisis.dto.ResultadoSentimiento;
import cl.mti.tesina.analisis.entities.Noticias;
import cl.mti.tesina.analisis.repositories.NoticiasRepository;
import cl.mti.tesina.analisis.services.AnalizadorSentimientosService;

@Component
public class WorkerAnalisis implements CommandLineRunner
{
	private static final Logger log = LoggerFactory.getLogger(WorkerAnalisis.class);

	@Autowired
	private NoticiasRepository noticiasRepository;

	@Autowired
	// @Qualifier("stanfordNlpSentimientosService")
	@Qualifier("amazonComprehendSentimientosService")
	private AnalizadorSentimientosService analizadorSentimientosService;

	@Override
	public void run(String... args) throws Exception
	{
		PageRequest request = PageRequest.of(0, 10);

		Page<Noticias> listadoNoticias = noticiasRepository.findAll(request);

		int i = 0;
		for (Noticias n : listadoNoticias)
		{
			i++;
			if (i < 8)
			{
				continue;
			}

			ResultadoSentimiento resultado = analizadorSentimientosService.procesar(n.getTexto(), "en");
			if (resultado.getCodigoResultado() == CodigosResultados.OK)
			{
				log.info("" + resultado.getSentimiento());
				log.info("" + resultado);
			}
			else
			{
				log.info("No se pudo determinar");
			}

		}

	}
}
