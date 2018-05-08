package cl.mti.tesina.pricing.runner;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import cl.mti.tesina.pricing.entities.Noticias;
import cl.mti.tesina.pricing.entities.Stocks;
import cl.mti.tesina.pricing.repositories.NoticiasRepository;
import cl.mti.tesina.pricing.repositories.StocksRepository;

@Component
public class EstimadorCostoCommandLineRunner implements CommandLineRunner
{
	private static final Logger log = LoggerFactory.getLogger(EstimadorCostoCommandLineRunner.class);

	@Autowired
	private NoticiasRepository noticiasRepository;
	@Autowired
	private StocksRepository stockRepository;
	private int tamanoPagina = 1000;
	private int numeroPagina = 0;

	@Override
	public void run(String... args) throws Exception
	{
		int tweets = 1315;
		long cantidadTotalUnidades = 0;

		PageRequest request = PageRequest.of(numeroPagina, tamanoPagina);

		Page<Stocks> paginaNoticias = stockRepository.findAll(request);

		int cantidadPaginas = paginaNoticias.getTotalPages();

		for (numeroPagina = 0; numeroPagina < cantidadPaginas; numeroPagina++)
		{
			if (numeroPagina > 0)
			{
				request = PageRequest.of(numeroPagina, tamanoPagina);
				paginaNoticias = stockRepository.findAll(request);
			}

			for (Stocks a : paginaNoticias)
			{
				Optional<Noticias> n = noticiasRepository.findById(a.getStocksId().getId());
				if (n.isPresent())
				{
					Noticias nn = n.get();
					if (nn.getFuente().equals("Twitter"))
					{
						tweets--;
						if (tweets == 0)
						{
							continue;
						}
					}

					String textoNoticia = nn.getTextoProcesado();

					int largoNoticia = textoNoticia.length();

					int cantidadUnidades = 3;
					if (largoNoticia > 300)
					{
						cantidadUnidades = (int) Math.ceil(largoNoticia / 300);
					}

					cantidadTotalUnidades += cantidadUnidades;
				}
			}
		}

		log.info("Total USD: " + (cantidadTotalUnidades * 0.0001));

	}

}
