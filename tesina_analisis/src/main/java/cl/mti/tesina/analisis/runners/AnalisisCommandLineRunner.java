package cl.mti.tesina.analisis.runners;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import cl.mti.tesina.analisis.dto.Estadisticas;
import cl.mti.tesina.analisis.entities.Stocks;
import cl.mti.tesina.analisis.repositories.StocksRepository;
import cl.mti.tesina.analisis.workers.WorkerStanford;

@Component
public class AnalisisCommandLineRunner implements CommandLineRunner
{
	private static final Logger log = LoggerFactory.getLogger(AnalisisCommandLineRunner.class);

	@Autowired
	private StocksRepository stockRepository;
	@Autowired
	private Estadisticas estadisticas;
	@Autowired
	private ApplicationContext applicationContext;

	@Override
	public void run(String... args) throws Exception
	{
		int pageNumber = 0;
		int size = 100;

		ExecutorService executors = Executors.newFixedThreadPool(40);

		PageRequest pageRequest = PageRequest.of(0, size);
		Page<Stocks> paginaNoticias = stockRepository.findAll(pageRequest);

		while (pageNumber < paginaNoticias.getTotalPages())
		{
			WorkerStanford w = (WorkerStanford) applicationContext.getBean("workerStanford");
			w.setNumeroPagina(pageNumber++);
			w.setTamanoPagina(size);
			w.setEstadisticas(estadisticas);
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

	}

}
