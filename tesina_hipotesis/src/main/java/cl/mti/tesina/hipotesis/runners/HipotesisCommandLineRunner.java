package cl.mti.tesina.hipotesis.runners;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import cl.mti.tesina.hipotesis.entities.Noticias;
import cl.mti.tesina.hipotesis.entities.Precios;
import cl.mti.tesina.hipotesis.entities.Resumen;
import cl.mti.tesina.hipotesis.entities.Sentimientos;
import cl.mti.tesina.hipotesis.entities.Stocks;
import cl.mti.tesina.hipotesis.repositories.NoticiasRepository;
import cl.mti.tesina.hipotesis.repositories.PreciosRepository;
import cl.mti.tesina.hipotesis.repositories.ResumenRepository;
import cl.mti.tesina.hipotesis.repositories.SentimientosRepository;
import cl.mti.tesina.hipotesis.repositories.StocksRepository;

@Component
public class HipotesisCommandLineRunner implements CommandLineRunner
{
	private static final Logger log = LoggerFactory.getLogger(HipotesisCommandLineRunner.class);

	private static final String FACEBOOK = "Facebook";
	private static final String MICROSOFT = "Microsoft";
	private static final String AMAZON = "Amazon";
	private static final String APPLE = "Apple";

	@Autowired
	private PreciosRepository preciosRepository;
	@Autowired
	private NoticiasRepository noticiasRepository;
	@Autowired
	private StocksRepository stocksRepository;
	@Autowired
	private SentimientosRepository sentimientosRepository;

	@Autowired
	private ResumenRepository resumenRepository;

	private final static int TAMANO_PAGINA = 1000;

	@Value("#{T(Float).parseFloat('${tesina.umbral}')}")
	private float umbral;

	@Override
	public void run(String... args) throws Exception
	{
		log.info("Procesando Hipotesis");

		PageRequest request = PageRequest.of(0, TAMANO_PAGINA);
		Page<Stocks> paginaNoticias = stocksRepository.findAll(request);

		int fin = paginaNoticias.getTotalPages();
		// 0: Amazon, 2: TextBlob, 4: Vader, 8: Opinion Finder
		List<Integer> listadoMotores = Stream.of(0, 2, 4, 8).collect(Collectors.toList());

		for (Integer idMotor : listadoMotores)
		{
			for (int numeroPagina = 0; numeroPagina < fin; numeroPagina++)
			{
				log.info("Procesando Pagina: " + numeroPagina + " de " + fin + " de motor " + idMotor);
				if (numeroPagina > 0)
				{
					request = PageRequest.of(numeroPagina, TAMANO_PAGINA);
					paginaNoticias = stocksRepository.findAll(request);
				}

				for (Stocks stock : paginaNoticias)
				{
					// Se obtiene la noticia asociada
					Noticias nn = noticiasRepository.getOne(stock.getStocksId().getId());
					// y el Listado de Sentimientos Asociados
					List<Sentimientos> listadoSentimientos = sentimientosRepository.findByIdMotorAndNoticia(idMotor,
							nn);
					Sentimientos ss = null;
					if (listadoSentimientos.size() == 1)
					{
						ss = listadoSentimientos.get(0);
					}

					if (ss == null)
					{
						continue;
					}
					MultiValuedMap<Date, Resumen> multiMap = new ArrayListValuedHashMap<Date, Resumen>();

					String accion = stock.getStocksId().getFuente();
					Resumen rr = new Resumen();
					rr.setIdMotor(idMotor);
					rr.setAccion(accion);
					rr.setUmbral(umbral);
					rr.setFecha(nn.getFecha());

					boolean isOk = false;
					// Si el sentimiento paso el umbral, vemos su precio
					if ((ss.getPuntajePositivo() > umbral) && (ss.getPuntajeNegativo() > umbral))
					{
						// Si las dos pasan el umbral dejaremos la mayor
						if (ss.getPuntajePositivo() > ss.getPuntajeNegativo())
						{
							ss.setPuntajeNegativo(0f);
						}
						else
						{
							ss.setPuntajePositivo(0f);
						}
					}
					// Noticia Positiva
					if (ss.getPuntajePositivo() > umbral)
					{
						rr.setSentimiento("Positivo");
						List<Precios> listadoPrecios = preciosRepository.findByAccionAndFecha(accion,
								ss.getNoticia().getFecha());

						for (Precios pp : listadoPrecios)
						{
							isOk = true;
							float porcen = pp.getClose() - pp.getOpen();
							if (porcen > 0)
							{
								rr.setOk(true);
							}
							else
							{
								rr.setOk(false);
							}
						}
					}
					// Noticia Positiva
					if (ss.getPuntajeNegativo() > umbral)
					{
						rr.setSentimiento("Negativo");
						List<Precios> listadoPrecios = preciosRepository.findByAccionAndFecha(accion,
								ss.getNoticia().getFecha());

						for (Precios pp : listadoPrecios)
						{
							isOk = true;
							float porcen = pp.getClose() - pp.getOpen();
							if (porcen < 0)
							{
								rr.setOk(true);
							}
							else
							{
								rr.setOk(false);
							}
						}
					}

					if (isOk)
					{
						resumenRepository.save(rr);
					}
				}
			}
		}
	}

}
