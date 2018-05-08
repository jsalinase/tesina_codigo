package cl.mti.tesina.analisis.runners;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import cl.mti.tesina.analisis.dto.Estadisticas;
import cl.mti.tesina.analisis.entities.Errores;
import cl.mti.tesina.analisis.entities.Stocks;
import cl.mti.tesina.analisis.repositories.ErroresRepository;
import cl.mti.tesina.analisis.repositories.StocksRepository;
import cl.mti.tesina.analisis.workers.WorkerAmazon;
import cl.mti.tesina.analisis.workers.WorkerStanford;
import cl.mti.tesina.analisis.workers.WorkerStanfordErrores;

@Component
public class AnalisisCommandLineRunner implements CommandLineRunner
{
	private static final Logger log = LoggerFactory.getLogger(AnalisisCommandLineRunner.class);

	@Autowired
	private StocksRepository stockRepository;
	@Autowired
	private ErroresRepository erroresRepository;
	@Autowired
	private Estadisticas estadisticasStanford;
	@Autowired
	private Estadisticas estadisticasAmazon;
	@Autowired
	private ApplicationContext applicationContext;
	@Value("#{new Boolean('${ejecutar_stanford}')}")
	private boolean ejecutarStanford;
	@Value("#{new Boolean('${ejecutar_amazon}')}")
	private boolean ejecutarAmazon;
	@Value("#{new Boolean('${ejecutar_errores}')}")
	private boolean ejecutarErrores;

	@Override
	public void run(String... args) throws Exception
	{
		int pageNumber = 0;
		int size = 100;

		if (ejecutarErrores)
		{
			log.info("Reprocesando Errores Stanford");

			ExecutorService executors = Executors.newFixedThreadPool(5);

			PageRequest pageRequest = PageRequest.of(pageNumber, size);
			Page<Errores> paginaErrores = erroresRepository.findAll(pageRequest);
			int totalPaginas = paginaErrores.getTotalPages();
			for (pageNumber = 0; pageNumber < totalPaginas; pageNumber++)
			{
				if (pageNumber > 0)
				{
					log.info("Pagina: " + pageNumber + " de " + totalPaginas);
					paginaErrores = erroresRepository.findAll(pageRequest);
				}

				for (Errores ee : paginaErrores)
				{
					WorkerStanfordErrores w = (WorkerStanfordErrores) applicationContext
							.getBean("workerStanfordErrores");
					w.setIdNoticia(ee.getId());
					w.setEstadisticas(estadisticasStanford);
					executors.execute(w);
				}
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
			log.info("" + estadisticasStanford);
			log.info("Fin Reprocesando Errores Stanford");

			estadisticasStanford = new Estadisticas();

		}
		if (ejecutarStanford)
		{
			log.info("Ejecutando Stanford");

			ExecutorService executors = Executors.newFixedThreadPool(5);

			PageRequest pageRequest = PageRequest.of(pageNumber, size);
			Page<Stocks> paginaNoticias = stockRepository.findAll(pageRequest);

			while (pageNumber < paginaNoticias.getTotalPages())
			{
				WorkerStanford w = (WorkerStanford) applicationContext.getBean("workerStanford");
				w.setNumeroPagina(pageNumber++);
				w.setTamanoPagina(size);
				w.setEstadisticas(estadisticasStanford);
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
			log.info("" + estadisticasStanford);
			log.info("Fin Stanford");
		}

		if (ejecutarAmazon)
		{
			log.info("Ejecutando Amazon");
			ExecutorService executors = Executors.newFixedThreadPool(5);
			pageNumber = 0;
			PageRequest pageRequest = PageRequest.of(pageNumber, size);
			Page<Stocks> paginaNoticias = stockRepository.findAll(pageRequest);

			while (pageNumber < paginaNoticias.getTotalPages())
			{
				WorkerAmazon w = (WorkerAmazon) applicationContext.getBean("workerAmazon");
				w.setNumeroPagina(pageNumber++);
				w.setTamanoPagina(size);
				w.setEstadisticas(estadisticasAmazon);
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

			log.info("" + estadisticasAmazon);
			log.info("Fin Amazon");
		}

	}

}
