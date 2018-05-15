package cl.mti.tesina.opinionfinder.runners;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import cl.mti.tesina.opinionfinder.Estadisticas;
import cl.mti.tesina.opinionfinder.entities.Stocks;
import cl.mti.tesina.opinionfinder.repositories.StocksRepository;
import cl.mti.tesina.opinionfinder.workers.WorkerOpinionFinder;

@Component
@ConfigurationProperties(prefix = "tesina")
public class OpinionFinderCommandLineRunner implements CommandLineRunner
{
	private static final Logger log = LoggerFactory.getLogger(OpinionFinderCommandLineRunner.class);

	@Value("${tesina.ruta_archivos}")
	private String rutaArchivos;
	@Autowired
	private StocksRepository stocksRepository;
	@Autowired
	private ApplicationContext applicationContext;
	@Autowired
	private Estadisticas estadisticas;

	private static final int TAMANO_PAGINA = 100;

	@Override
	public void run(String... args) throws Exception
	{
		log.info("Procesando Opinion Finder");

		File f = new File(rutaArchivos);

		if (!f.isDirectory())
		{
			log.info("Ruta: " + rutaArchivos + " no es un directorio, saliendo");
			return;
		}
		else
		{
			log.info("Usando Ruta: " + rutaArchivos);
		}

		PageRequest request = PageRequest.of(0, TAMANO_PAGINA);
		Page<Stocks> paginaNoticias = stocksRepository.findAll(request);

		int cantidadPaginas = paginaNoticias.getTotalPages();

		ExecutorService executors = Executors.newFixedThreadPool(8);

		for (int numeroPagina = 0; numeroPagina < cantidadPaginas; numeroPagina++)
		{
			WorkerOpinionFinder w = (WorkerOpinionFinder) applicationContext.getBean("workerOpinionFinder");
			w.setF(f);
			w.setNumeroPagina(numeroPagina);
			executors.execute(w);
		}
		executors.shutdown();
		try
		{
			if (!executors.awaitTermination(Integer.MAX_VALUE, TimeUnit.MINUTES))
			{
				executors.shutdownNow();
			}
		}
		catch (InterruptedException ex)
		{
			executors.shutdownNow();
			Thread.currentThread().interrupt();
		}

		log.info("" + estadisticas);
		log.info("Fin Reprocesando Errores Stanford");
	}
}
