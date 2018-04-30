package cl.mti.tesina.cleanseload.workers;

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

import cl.mti.tesina.cleanseload.entities.Noticias;
import cl.mti.tesina.cleanseload.repositories.NoticiasRepository;

@Component
public class CleanseLoadNoticias implements CommandLineRunner
{
	private static final Logger log = LoggerFactory.getLogger(CleanseLoadNoticias.class);

	@Autowired
	private NoticiasRepository noticiasRepository;
	@Autowired
	private ApplicationContext applicationContext;

	@Override
	public void run(String... args) throws Exception
	{
		int pageNumber = 0;
		int size = 1000;

		ExecutorService executors = Executors.newFixedThreadPool(10);

		PageRequest pageRequest = PageRequest.of(0, size);
		Page<Noticias> paginaNoticias = noticiasRepository.findAll(pageRequest);

		while (pageNumber < paginaNoticias.getTotalPages())
		{
			Worker w = (Worker) applicationContext.getBean("worker");
			w.setNumeroPagina(pageNumber++);
			w.setTamanoPagina(size);
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
