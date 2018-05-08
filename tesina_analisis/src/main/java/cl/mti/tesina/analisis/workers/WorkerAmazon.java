package cl.mti.tesina.analisis.workers;

import java.util.Date;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import cl.mti.tesina.analisis.dto.CodigosResultados;
import cl.mti.tesina.analisis.dto.Estadisticas;
import cl.mti.tesina.analisis.dto.Idiomas;
import cl.mti.tesina.analisis.dto.ResultadoSentimiento;
import cl.mti.tesina.analisis.entities.Errores;
import cl.mti.tesina.analisis.entities.Noticias;
import cl.mti.tesina.analisis.entities.SentimientoAmazon;
import cl.mti.tesina.analisis.entities.Sentimientos;
import cl.mti.tesina.analisis.entities.Stocks;
import cl.mti.tesina.analisis.repositories.ErroresRepository;
import cl.mti.tesina.analisis.repositories.NoticiasRepository;
import cl.mti.tesina.analisis.repositories.SentimientoAmazonRepository;
import cl.mti.tesina.analisis.repositories.SentimientosRepository;
import cl.mti.tesina.analisis.repositories.StocksRepository;
import cl.mti.tesina.analisis.services.AnalizadorSentimientosService;

@Component("workerAmazon")
@Scope("prototype")
public class WorkerAmazon implements Runnable
{
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	@Autowired
	@Qualifier("amazonComprehendSentimientosService")
	private AnalizadorSentimientosService analizadorSentimientosService;
	@Autowired
	private NoticiasRepository noticiasRepository;
	@Autowired
	private SentimientosRepository sentimientosRepository;
	@Autowired
	private ErroresRepository erroresRepository;
	@Autowired
	private StocksRepository stocksRepository;
	private Estadisticas estadisticas;
	private int tamanoPagina;
	private int numeroPagina;
	@Value("#{new Boolean('${guardar_traza_amazon}')}")
	private boolean guardarTraza;
	@Autowired
	private SentimientoAmazonRepository sentimientoAmazonRepository;

	@Override
	public void run()
	{
		log.info("Empezando Pagina " + numeroPagina);

		PageRequest request = PageRequest.of(numeroPagina, tamanoPagina);

		Page<Stocks> paginaNoticias = stocksRepository.findAll(request);

		for (Stocks a : paginaNoticias)
		{
			estadisticas.incrementLeidos();
			Optional<Noticias> n = noticiasRepository.findById(a.getStocksId().getId());
			if (n.isPresent())
			{
				procesar(n.get());
			}
		}

		log.info("Terminando Pagina " + numeroPagina);
	}

	private void procesar(Noticias n)
	{
		try
		{
			ResultadoSentimiento resultado = analizadorSentimientosService.procesar(n.getTexto(), Idiomas.INGLES);

			if (guardarTraza)
			{
				try
				{
					SentimientoAmazon sa = new SentimientoAmazon();
					sa.setId(n.getId());
					sa.setSentimiento(resultado.getSentimiento());
					sa.setMixto(resultado.getMixto());
					sa.setNegativo(resultado.getNegativo());
					sa.setNeutral(resultado.getNeutral());
					sa.setPositivo(resultado.getPositivo());
					sa.setFechaHora(new Date());
					sa.setIdNoticia(n.getId());
					sentimientoAmazonRepository.save(sa);
				}
				catch (Exception ex)
				{

				}
			}

			if (resultado.getCodigoResultado() == CodigosResultados.OK)
			{
				Sentimientos s = new Sentimientos();

				s.setIdMotor(analizadorSentimientosService.getIdMotor());
				s.setNoticia(n);
				s.setSentimiento(resultado.getSentimiento());
				s.setPuntajePositivo(resultado.getPositivo());
				s.setPuntajeNegativo(resultado.getNegativo());
				s.setPuntajeNeutral(resultado.getNeutral());
				s.setPuntajeMixto(resultado.getMixto());
				sentimientosRepository.save(s);

				estadisticas.incrementGrabados();
			}
			else
			{
				try
				{
					Errores e = new Errores();
					e.setId(n.getId());
					erroresRepository.save(e);
					estadisticas.incrementErrores();
				}
				catch (Exception ex)
				{

				}
			}
		}
		catch (Exception ex)
		{
			Errores e = new Errores();
			e.setId(n.getId());
			erroresRepository.save(e);
			estadisticas.incrementErrores();
		}

	}

	public Estadisticas getEstadisticas()
	{
		return estadisticas;
	}

	public void setEstadisticas(Estadisticas estadisticas)
	{
		this.estadisticas = estadisticas;
	}

	public int getTamanoPagina()
	{
		return tamanoPagina;
	}

	public void setTamanoPagina(int tamanoPagina)
	{
		this.tamanoPagina = tamanoPagina;
	}

	public int getNumeroPagina()
	{
		return numeroPagina;
	}

	public void setNumeroPagina(int numeroPagina)
	{
		this.numeroPagina = numeroPagina;
	}

	public boolean isGuardarTraza()
	{
		return guardarTraza;
	}

	public void setGuardarTraza(boolean guardarTraza)
	{
		this.guardarTraza = guardarTraza;
	}

}
